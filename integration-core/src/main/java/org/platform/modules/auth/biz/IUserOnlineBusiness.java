package org.platform.modules.auth.biz;

import java.util.Date;
import java.util.List;

import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.UserOnline;

public interface IUserOnlineBusiness extends IGenericBusiness<UserOnline, String> {

	/**
     * 上线
     * @param userOnline
     */
    public void online(UserOnline userOnline);

    /**
     * 下线
     * @param id
     */
    public void offline(String id);

    /**
     * 批量下线
     * @param needOfflineIdList
     */
    public void batchOffline(List<String> needOfflineIdList);

    /**
     * 无效的UserOnline
     * @return
     */
    public QueryResult<UserOnline> findExpiredUserOnlineList(Date expiredDate);
}
