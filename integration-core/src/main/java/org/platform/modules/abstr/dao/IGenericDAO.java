package org.platform.modules.abstr.dao;

import java.io.Serializable;
import java.util.List;

import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.utils.exception.DataAccessException;

public interface IGenericDAO<Entity extends Serializable, PK extends Serializable> {

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：insert</p>
	  *<p>描述：新增实体对象</p>
	  *<p>参数：@param entity 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:00:28</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void insert(Entity entity) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：update</p>
	  *<p>描述：修改实体对象</p>
	  *<p>参数：@param entity 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:01:03</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void update(Entity entity) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：delete</p>
	  *<p>描述：删除实体对象</p>
	  *<p>参数：@param entity 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:13:12</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void delete(Entity entity) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：deleteById</p>
	  *<p>描述：根据主键删除实体对象</p>
	  *<p>参数：@param pk 主键
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:13:40</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void deleteByPK(PK pk) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：readDataByPK</p>
	  *<p>描述：根据主键读取实体对象</p>
	  *<p>参数：@param pk 主键
	  *<p>参数：@return 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：Entity</p>
	  *<p>创建时间：2013-4-8 下午11:16:13</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public Entity readDataByPK(PK pk) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：readDataByCondition</p>
	  *<p>描述：读取实体对象</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@return 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：Entity</p>
	  *<p>创建时间：2013-4-8 下午11:23:21</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public Entity readDataByCondition(QueryCondition condition) throws DataAccessException;
	
	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：readDataListByCondition</p>
	  *<p>描述：读取实体对象列表</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@return 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：QueryResult<Entity></p>
	  *<p>创建时间：2013-4-8 下午11:23:57</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public List<Entity> readDataListByCondition(QueryCondition condition) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：readDataPaginationByCondition</p>
	  *<p>描述：读取实体对象分页列表</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@return 实体
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：QueryResult<Entity></p>
	  *<p>创建时间：2013-4-8 下午11:23:57</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public QueryResult<Entity> readDataPaginationByCondition(QueryCondition condition) throws DataAccessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.dao.IGenericDAO</p>
	  *<p>方法名：flush</p>
	  *<p>描述：刷新</p>
	  *<p>参数：@throws DataAccessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:25:09</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void flush() throws DataAccessException;
}
