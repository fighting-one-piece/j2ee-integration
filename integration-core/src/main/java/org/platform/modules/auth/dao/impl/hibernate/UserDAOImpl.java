package org.platform.modules.auth.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userHibernateDAO")
public class UserDAOImpl extends GenericHibernateDAOImpl<User, Long> implements IUserDAO {

	
}
