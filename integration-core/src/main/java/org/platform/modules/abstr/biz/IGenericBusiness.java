package org.platform.modules.abstr.biz;

import java.io.Serializable;
import java.util.List;

import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.utils.exception.BusinessException;

public interface IGenericBusiness<Entity extends Serializable, PK extends Serializable> {

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：insert</p>
	  *<p>描述：保存对象</p>
	  *<p>参数：@param object 对象
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:00:28</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void insert(Object object) throws BusinessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：update</p>
	  *<p>描述：修改对象</p>
	  *<p>参数：@param object 对象
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:01:03</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void update(Object object) throws BusinessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：deleteByPK</p>
	  *<p>描述：根据主键删除实体对象</p>
	  *<p>参数：@param pk 主键
	  *<p>参数：@throws BusinessException</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-8 下午11:13:40</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public void deleteByPK(PK pk) throws BusinessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：readDataByPK</p>
	  *<p>描述：根据主键读取实体对象</p>
	  *<p>参数：@param pk 主键
	  *<p>参数：@param isConvert 是否转换实体
	  *<p>参数：@return 实体
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：Entity</p>
	  *<p>创建时间：2013-4-8 下午11:16:13</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public Object readDataByPK(PK pk, boolean isConvert) throws BusinessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：readDataByCondition</p>
	  *<p>描述：读取对象</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@param isConvert 是否转换实体
	  *<p>参数：@return 对象
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：Object</p>
	  *<p>创建时间：2013-4-8 下午11:23:21</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public Object readDataByCondition(QueryCondition condition, boolean isConvert) throws BusinessException;

	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：readDataListByCondition</p>
	  *<p>描述：读取对象列表</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@param isConvert 是否转换实体
	  *<p>参数：@return 对象列表
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：QueryResult<Entity></p>
	  *<p>创建时间：2013-4-8 下午11:23:57</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public List<?> readDataListByCondition(QueryCondition condition, boolean isConvert) throws BusinessException;
	
	/**
	 *
	  *<p>包名类名：org.platform.modules.abstr.biz.IGenericBusiness</p>
	  *<p>方法名：readDataPaginationByCondition</p>
	  *<p>描述：读取对象分页列表</p>
	  *<p>参数：@param condition 条件
	  *<p>参数：@param isConvert 是否转换实体
	  *<p>参数：@return 对象列表
	  *<p>参数：@throws BusinessException 异常</p>
	  *<p>返回类型：QueryResult<Entity></p>
	  *<p>创建时间：2013-4-8 下午11:23:57</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public QueryResult<?> readDataPaginationByCondition(QueryCondition condition, boolean isConvert) throws BusinessException;
}
