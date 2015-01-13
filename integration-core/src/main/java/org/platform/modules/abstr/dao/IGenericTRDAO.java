package org.platform.modules.abstr.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface IGenericTRDAO<Entity extends Serializable, PK extends Serializable> extends IGenericDAO<Entity, PK> {

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
	 * 根据thing1Id、thing2Id、kind读取数据
	 * @param thing1Id
	 * @param thing2Id
	 * @param kind
	 * @return
	 * @throws DataAccessException
	 */
	public Entity readDataByThing1IdAndThing2IdAndKind(Long thing1Id, 
			Long thing2Id, String kind) throws DataAccessException;
	
	/**
	 * 根据thing2Id、kind读取Thing1Id数据集合
	 * @param thing2Id
	 * @param kind
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> readThing1IdsByThing2IdAndKind(Long thing2Id, 
			String kind) throws DataAccessException;

	/**
	 * 根据thing1Id、kind读取Thing2Id数据集合
	 * @param thing1Id
	 * @param kind
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> readThing2IdsByThing1IdAndKind(Long thing1Id, 
			String kind) throws DataAccessException;
}
