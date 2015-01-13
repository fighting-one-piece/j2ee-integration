package org.platform.modules.abstr.dao;

import java.io.Serializable;

import org.springframework.dao.DataAccessException;

public interface IGenericTSDAO<Entity extends Serializable, PK extends Serializable> extends IGenericDAO<Entity, PK> {

	/**
	 * 根据主键修改顶增数
	 * @param pk
	 * @param incr
	 * @throws DataAccessException
	 */
	public void updateUpIncr(PK pk, int incr) throws DataAccessException;
	
	/**
	 * 根据主键修改踩增数
	 * @param pk
	 * @param incr
	 * @throws DataAccessException
	 */
	public void updateDownIncr(PK pk, int incr) throws DataAccessException;
	
	/**
	 * 根据主键修改删除标记
	 * @param pk
	 * @param deleted
	 * @throws DataAccessException
	 */
	public void updateDeleted(PK pk, boolean deleted) throws DataAccessException;
	
	/**
	 * 根据主键修改垃圾标记
	 * @param pk
	 * @param spam
	 * @throws DataAccessException
	 */
	public void updateSpam(PK pk, boolean spam) throws DataAccessException;
	
	/**
	 * 根据主键修改整型属性增量数
	 * @param pk
	 * @param attribute
	 * @param incr
	 * @throws DataAccessException
	 */
	public void updateIntAttributeIncr(PK pk, String attribute, int incr) throws DataAccessException;
	
	/**
	 * 根据主键读取顶数
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readUpsByPK(Long id) throws DataAccessException;
	
	/**
	 * 根据主键读取踩数
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readDownsByPK(Long id) throws DataAccessException;

}
