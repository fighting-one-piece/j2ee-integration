package org.platform.modules.auth.biz.impl;

import java.util.Date;
import java.util.List;

import org.platform.entity.Query;
import org.platform.entity.QueryItem;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.auth.biz.IUserOnlineBusiness;
import org.platform.modules.auth.entity.UserOnline;
import org.springframework.stereotype.Service;

@Service("userOnlineBusiness")
public class UserOnlineBusinessImpl extends GenericBusinessImpl<UserOnline, String> 
	implements IUserOnlineBusiness {

	@Override
	public void online(UserOnline userOnline) {
		insert(userOnline);
	}
	
	@Override
	public void offline(String id) {
		if (null == id) {
			deleteByPK(id);
		}
	}
	
	@Override
	public void batchOffline(List<String> needOfflineIdList) {
		for (String id : needOfflineIdList) {
			offline(id);
		}
	}
	
	@Override
	public QueryResult<UserOnline> findExpiredUserOnlineList(Date expiredDate) {
		Query condition = new Query();
		condition.addHibernateCondition("", expiredDate, QueryItem.MATCH_LE);
		return null;
	}
}
