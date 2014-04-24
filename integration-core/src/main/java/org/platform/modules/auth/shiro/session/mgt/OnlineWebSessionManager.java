package org.platform.modules.auth.shiro.session.mgt;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.platform.modules.auth.Constants;
import org.platform.modules.auth.biz.IUserOnlineBusiness;
import org.platform.modules.auth.shiro.session.OnlineSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 为OnlineSession定制的Web Session Manager
 * 主要是在此如果会话的属性修改了 就标识下其修改了 然后方便 OnlineSessionDao同步
 */
public class OnlineWebSessionManager extends DefaultWebSessionManager {

    private static final Logger log = LoggerFactory.getLogger(OnlineWebSessionManager.class);

    @Resource(name = "userOnlineBusiness")
    private IUserOnlineBusiness userOnlineBusiness = null;
    
    public void setUserOnlineBusiness(IUserOnlineBusiness userOnlineBusiness) {
		this.userOnlineBusiness = userOnlineBusiness;
	}

	@Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
        super.setAttribute(sessionKey, attributeKey, value);
        if (value != null && needMarkAttributeChanged(attributeKey)) {
            OnlineSession s = (OnlineSession) doGetSession(sessionKey);
            s.markAttributeChanged();
        }
    }

    private boolean needMarkAttributeChanged(Object attributeKey) {
        if(attributeKey == null) {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        //优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework")) {
            return false;
        }
        if(attributeKeyStr.startsWith("javax.servlet")) {
            return false;
        }
        if(attributeKeyStr.equals(Constants.CURRENT_USERNAME)) {
            return false;
        }
        return true;
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null) {
            OnlineSession s = (OnlineSession) doGetSession(sessionKey);
            s.markAttributeChanged();
        }

        return removed;
    }

    /**
     * 验证session是否有效 用于删除过期session
     */
    @Override
    public void validateSessions() {
        if (log.isInfoEnabled()) {
            log.info("invalidation sessions...");
        }

        int invalidCount = 0;

//        int timeout = (int) getGlobalSessionTimeout();
//        Date expiredDate = DateUtils.addMilliseconds(new Date(), 0 - timeout);
//        Page<UserOnline> page = userOnlineBusiness.findExpiredUserOnlineList(expiredDate);
//
//        List<String> needOfflineIdList = Lists.newArrayList();
        //改成批量过期删除
//        while (page.hasContent()) {
//            for (UserOnline userOnline : page.getContent()) {
//                try {
//                    SessionKey key = new DefaultSessionKey(userOnline.getId());
//                    Session session = retrieveSession(key);
//                    //仅从cache中删除 db的删除
//                    if (session != null) {
//                        session.setAttribute(Constants.ONLY_CLEAR_CACHE, true);
//                    }
//                    validate(session, key);
//                } catch (InvalidSessionException e) {
//                    if (log.isDebugEnabled()) {
//                        boolean expired = (e instanceof ExpiredSessionException);
//                        String msg = "Invalidated session with id [" + userOnline.getId() + "]" +
//                                (expired ? " (expired)" : " (stopped)");
//                        log.debug(msg);
//                    }
//                    invalidCount++;
//                    needOfflineIdList.add(userOnline.getId());
//                }
//
//                if (needOfflineIdList.size() > 0) {
//                    try {
//                    	userOnlineBusiness.batchOffline(needOfflineIdList);
//                    } catch (Exception e) {
//                        log.error("batch delete db session error.", e);
//                    }
//                }
//            }
//
//            pageRequest = new PageRequest((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize(), pageRequest.getPageSize());
//            page = userOnlineBusiness.findExpiredUserOnlineList(expiredDate);
//        }

        if (log.isInfoEnabled()) {
            String msg = "Finished invalidation session.";
            if (invalidCount > 0) {
                msg += "  [" + invalidCount + "] sessions were stopped.";
            } else {
                msg += "  No sessions were stopped.";
            }
            log.info(msg);
        }

    }

    @Override
    protected Collection<Session> getActiveSessions() {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }
}
