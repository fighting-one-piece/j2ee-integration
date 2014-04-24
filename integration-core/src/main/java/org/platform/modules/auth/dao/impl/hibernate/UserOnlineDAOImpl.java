package org.platform.modules.auth.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IUserOnlineDAO;
import org.platform.modules.auth.entity.UserOnline;
import org.springframework.stereotype.Repository;

@Repository("userOnlineDAO")
public class UserOnlineDAOImpl extends GenericHibernateDAOImpl<UserOnline, String> 
	implements IUserOnlineDAO {

}
