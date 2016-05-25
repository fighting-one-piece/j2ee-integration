package org.platform.modules.auth.dao.impl;

import org.platform.modules.abstr.dao.impl.GenericMyBatisDAOImpl;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAOImpl extends GenericMyBatisDAOImpl<User, Long> implements IUserDAO {

}
