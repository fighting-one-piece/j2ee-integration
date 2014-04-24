package org.platform.modules.auth.biz;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.User;
import org.platform.utils.exception.BusinessException;

public interface IUserBusiness extends IGenericBusiness<User, Long> {
	
	public User readDataByName(String name) throws BusinessException;

	public User readDataByNameAndPassword(String name, String password) throws BusinessException;
}
