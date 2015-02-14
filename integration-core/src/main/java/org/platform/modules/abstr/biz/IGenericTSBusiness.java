package org.platform.modules.abstr.biz;

import java.io.Serializable;
import java.util.List;

import org.platform.utils.exception.BusinessException;
import org.springframework.dao.DataAccessException;

public interface IGenericTSBusiness<Entity extends Serializable, PK extends Serializable> extends IGenericBusiness<Entity, PK> {
	
	/**
	 * 插入实体缓存
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	public Object insertCache(Entity entity) throws BusinessException;
	
	/**
	 * 插入实体缓存
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	public Object insertCache(Object entity) throws BusinessException;
	
	/**
	 * 插入实体缓存列表
	 * @param entities
	 * @return
	 * @throws BusinessException
	 */
	public List<?> insertCacheList(List<Entity> entities) throws BusinessException;
	
	/**
	 * 更新实体缓存
	 * @param entity
	 * @throws BusinessException
	 */
	public void updateCache(Entity entity) throws BusinessException;
	
	/**
	 * 根据主键修改顶增数加1并且新增实体用户关系
	 * @param pk
	 * @param userId
	 * @throws BusinessException
	 */
	public void updateUp(PK pk, Long userId) throws BusinessException;

	/**
	 * 根据主键修改顶增数
	 * @param pk
	 * @param incr
	 * @throws BusinessException
	 */
	public void updateUpIncr(PK pk, int incr) throws BusinessException;
	
	/**
	 * 根据主键修改踩增数加1并且新增实体用户关系
	 * @param pk
	 * @param userId
	 * @throws BusinessException
	 */
	public void updateDown(PK pk, Long userId) throws BusinessException;
	
	/**
	 * 根据主键修改踩增数
	 * @param pk
	 * @param incr
	 * @throws BusinessException
	 */
	public void updateDownIncr(PK pk, int incr) throws BusinessException;
	
	/**
	 * 根据主键修改删除标记
	 * @param pk
	 * @param deleted
	 * @throws BusinessException
	 */
	public void updateDeleted(PK pk, boolean deleted) throws BusinessException;
	
	/**
	 * 根据主键修改垃圾标记
	 * @param pk
	 * @param spam
	 * @throws BusinessException
	 */
	public void updateSpam(PK pk, boolean spam) throws BusinessException;
	
	/**
	 * 读取实体缓存
	 * @param identity
	 * @return
	 * @throws BusinessException
	 */
	public Object readCache(String identity) throws BusinessException;
	
	/**
	 * 读取实体缓存列表
	 * @param identities
	 * @return
	 * @throws BusinessException
	 */
	public List<?> readCacheList(List<String> identities) throws BusinessException;
	
	/**
	 * 根据主键读取顶数
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readUpsByPK(Long id) throws BusinessException;
	
	/**
	 * 根据主键读取踩数
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readDownsByPK(Long id) throws BusinessException;
	
}
