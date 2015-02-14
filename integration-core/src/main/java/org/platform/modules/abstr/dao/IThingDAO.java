package org.platform.modules.abstr.dao;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.entity.Thing;
import org.springframework.dao.DataAccessException;

public interface IThingDAO extends IGenericDAO<Thing, Long> {
	
	/**
	 * 根据主键修改顶增数
	 * @param table
	 * @param id
	 * @param incr
	 * @throws DataAccessException
	 */
	public void updateUpsIncr(String table, Long id, int incr) throws DataAccessException;
	
	/**
	 * 根据主键修改踩增数
	 * @param table
	 * @param id
	 * @param incr
	 * @throws DataAccessException
	 */
	public void updateDownsIncr(String table, Long id, int incr) throws DataAccessException;
	
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
	
	/**
	 * 根据主键读取顶数
	 * @param table
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readUpsByPK(String table, Long id) throws DataAccessException;
	
	/**
	 * 根据主键读取踩数
	 * @param table
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public int readDownsByPK(String table, Long id) throws DataAccessException;
	
	/**
	 * 根据主键读取主从数据
	 * @param thingTable
	 * @param dataTable
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public Thing readThingById(String thingTable, String dataTable, Long id) throws DataAccessException;
	
	/**
	 * 读取带排序的数据分页信息
	 * @param query
	 * @return
	 * @throws DataAccessException
	 */
	public QueryResult<Thing> readDataPaginationByConditionWithOrder(Query query) throws DataAccessException;
	
}
