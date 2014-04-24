package org.platform.modules.auth.dao;

import java.util.List;

import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.entity.Role;
import org.platform.utils.exception.DataAccessException;

public interface IRoleDAO extends IGenericDAO<Role, Long>{

	/**
	 * 根据用户ID获取角色列表
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List<Role> readDataListByUserId(Long userId) throws DataAccessException;
	
	/**
	 * 根据组ID获取角色列表
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<Role> readDataListByGroupId(Long groupId) throws DataAccessException;
}
