package org.platform.modules.abstr.dao;

import org.platform.modules.abstr.entity.ThingRelation;
import org.springframework.dao.DataAccessException;

public interface IThingRelationDAO extends IGenericDAO<ThingRelation, Long> {

	/**
	 * 根据主键修改删除标记
	 * @param table
	 * @param id
	 * @param deleted
	 * @throws DataAccessException
	 */
	public void updateDeleted(String table, Long id, boolean deleted) throws DataAccessException;
	
	/**
	 * 根据主键修改垃圾标记
	 * @param table
	 * @param id
	 * @param spam
	 * @throws DataAccessException
	 */
	public void updateSpam(String table, Long id, boolean spam) throws DataAccessException;
	
}
