package org.platform.modules.auth.extra.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.platform.modules.auth.entity.User;
import org.platform.utils.aspect.CacheAspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserCacheAspect extends CacheAspect {

	public UserCacheAspect() {
        setCacheName("SimpleCache");
    }

    private String idKeyPrefix = "userid-";
    private String nameKeyPrefix = "username-";

    /**匹配用户业务*/
    @Pointcut(value = "target(org.platform.modules.auth.biz.impl.UserBusinessImpl)")
    private void userBusinessPointcut() {
    }

    /**put 操作缓存*/
    @Pointcut(value ="execution(* insert(..)) || execution(* update(..)) ")
    private void cachePutPointcut() {
    }


    /**evict 删除*/
    @Pointcut(value = "(execution(* deleteByPK(*))) && args(arg)", argNames = "arg")
    private void cacheEvictPointcut(Object arg) {
    }

    /** get 操作缓存*/
    @Pointcut(value = "(execution(* readDataByPK(*)) || execution(* readDataByCondition(*)))")
    private void cacheGetPointcut() {
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////增强实现
    ////////////////////////////////////////////////////////////////////////////////
    @AfterReturning(value = "userBusinessPointcut() && cachePutPointcut()", returning = "user")
    public void cachePutAdvice(Object user) {
        put((User) user);
    }

    @After(value = "userBusinessPointcut() && cacheEvictPointcut(arg)", argNames = "arg")
    public void cacheEvictAdvice(Object arg) {
        if (arg == null) {
            return;
        }
        if (arg instanceof Long) {
            evictId(String.valueOf(arg));
        }
        if (arg instanceof Long[]) {
            for (Long id : (Long[]) arg) {
                evictId(String.valueOf(id));
            }
        }
        if (arg instanceof String) {
            evictId((String) arg);
        }
        if (arg instanceof String[]) {
            for (String id : (String[]) arg) {
                evictId(String.valueOf(id));
            }
        }
        if (arg instanceof User) {
            evict((User) arg);
        }
    }

    @Around(value = "userBusinessPointcut() && cacheGetPointcut()")
    public Object cacheGetAdvice(ProceedingJoinPoint pjp) throws Throwable {

        String methodName = pjp.getSignature().getName();
        Object arg = pjp.getArgs().length >= 1 ? pjp.getArgs()[0] : null;

        String key = "";
        boolean isIdKey = false;
        if ("readDataByPK".equals(methodName)) {
            key = idKey(String.valueOf(arg));
            isIdKey = true;
        } else if ("readDataByName".equals(methodName)) {
            key = nameKey((String) arg);
        } 
        User user = null;
        if (isIdKey == true) {
            user = get(key);
        } else {
            Long id = get(key);
            if (id != null) {
                key = idKey(String.valueOf(id));
                user = get(key);
            }
        }
        //cache hit
        if (user != null) {
            log.debug("cacheName:{}, hit key:{}", cacheName, key);
            return user;
        }
        log.debug("cacheName:{}, miss key:{}", cacheName, key);

        //cache miss
        user = (User) pjp.proceed();
        //put cache
        put(user);
        return user;
    }


    private String idKey(String id) {
        return idKeyPrefix + id;
    }

    private String nameKey(String username) {
        return nameKeyPrefix + username;
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////cache 抽象实现
    ////////////////////////////////////////////////////////////////////////////////
    public void put(User user) {
        if (user == null) {
            return;
        }
        Long id = user.getId();
        put(nameKey(user.getName()), id);
        // id ---> user
        put(idKey(String.valueOf(id)), user);
    }


    public void evictId(String id) {
        evict(idKey(id));
    }

    public void evict(User user) {
        if (user == null) {
            return;
        }
        Long id = user.getId();
        evict(idKey(String.valueOf(id)));
        evict(nameKey(user.getName()));
    }
    
}
