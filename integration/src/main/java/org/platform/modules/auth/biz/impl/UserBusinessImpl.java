package org.platform.modules.auth.biz.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.biz.IUserBusiness;
import org.platform.modules.auth.biz.converter.UserConverter;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("userBusiness") 
public class UserBusinessImpl extends GenericBusinessImpl<User, Long> implements IUserBusiness {
	
	private static Map<String, User> userMap = new HashMap<String, User>();

	@Resource(name = "userDAO")
	private IUserDAO userDAO = null;
	
	@Override
	public IGenericDAO<User, Long> obtainDAOInstance() {
		return userDAO;
	}

	@Override
	protected IConverter<?, ?> obtainConverter() {
		return UserConverter.getInstance();
	}

	@Override
	public User readDataByName(String name) throws BusinessException {
		User user = userMap.get(name);
		if (null == user) {
			Query condition = new Query();
			condition.addHibernateCondition("name", name);
			user = userDAO.readDataByCondition(condition);
			userMap.put(name, user);
		}
		if (null == user) {
			throw new BusinessException("用户不存在");
		}
		return user;
	}

	@Override
	public User readDataByNameAndPassword(String name, String password) throws BusinessException {
		User user = readDataByName(name);
		if (!password.equals(user.getPassword())) {
			throw new BusinessException("用户密码不正确");
		}
		return user;
	}
	
	
}
