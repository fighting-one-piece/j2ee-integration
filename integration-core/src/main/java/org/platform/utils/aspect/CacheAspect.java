package org.platform.utils.aspect;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 基础cache切面
 */
public class CacheAspect implements InitializingBean {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected String cacheName = null;
    
    private Cache cache = null;
    
    @Resource(name = "ehCacheManager")
    private CacheManager cacheManager = null;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache(cacheName);
    }

    /**
     * 缓存池名称
     * @param cacheName
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * 缓存管理器
     * @return
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    public void clear() {
        log.debug("cacheName:{}, cache clear", cacheName);
        this.cache.removeAll();
    }

    public void evict(String key) {
        log.debug("cacheName:{}, evict key:{}", cacheName, key);
        this.cache.remove(key);
    }

    @SuppressWarnings("unchecked")
	public <T> T get(Object key) {
        log.debug("cacheName:{}, get key:{}", cacheName, key);
        if (StringUtils.isEmpty((String) key)) {
            return null;
        }
        Element element = this.cache.get(key);
		return (T) (element != null ? element.getObjectValue() : null);
    }

    public void put(String key, Object value) {
        log.debug("cacheName:{}, put key:{}", cacheName, key);
        this.cache.put(new Element(key, value));
    }

}
