package org.platform.modules.auth.dao;

import java.util.List;

import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.entity.Group;
import org.platform.utils.exception.DataAccessException;

public interface IGroupDAO extends IGenericDAO<Group, Long>{

	/**
	 * 根据用户ID读取组列表
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List<Group> readDataListByUserId(Long userId) throws DataAccessException;
}
