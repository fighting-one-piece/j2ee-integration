package org.platform.modules.auth.dao.impl.mongo;

import org.platform.modules.abstr.dao.impl.GenericMongoDAOImpl;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userMongoDAO")
public class UserDAOImpl extends GenericMongoDAOImpl<User, Long> implements IUserDAO {

	
}
