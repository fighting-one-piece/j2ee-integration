package org.platform.modules.abstr.biz.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.BusinessException;
import org.platform.utils.spring.SpringUtils;

public abstract class GenericBusinessImpl<Entity extends Serializable, PK extends Serializable> implements IGenericBusiness<Entity, PK> {

	/** 日志*/
	protected Logger logger = Logger.getLogger(getClass());

	protected Class<Entity> entityClass = null;
	
	@SuppressWarnings("unchecked")
	public GenericBusinessImpl() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
	}

	protected IConverter<?,?> obtainConverter() {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected IGenericDAO<Entity, PK> obtainDAOInstance() {
		String className = getClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1,
				className.indexOf("BusinessImpl"));
		StringBuilder beanName = new StringBuilder();
		beanName.append(Character.toLowerCase(className.charAt(0)))
	  		.append(className.substring(1)).append("DAO");
		logger.debug("beanName: " + beanName.toString());
		Object daoInstance = SpringUtils.getBean(beanName.toString());
		if (null == daoInstance) {
			throw new BusinessException("数据访问对象获取失败");
		}
		return (IGenericDAO<Entity, PK>) daoInstance;
	}
	
	protected void preHandle(Object object) throws BusinessException {
		
	}
	
	protected void postHandle(Object object) throws BusinessException {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(Object object) throws BusinessException {
		preHandle(object);
		Entity entity = (Entity) object;
		if (!entityClass.isAssignableFrom(object.getClass())) {
			entity = (Entity) obtainConverter().convertObject(object);
		}
		obtainDAOInstance().insert(entity);
		postHandle(entity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Object object) throws BusinessException {
		preHandle(object);
		Entity entity = (Entity) object;
		if (!entityClass.isAssignableFrom(object.getClass())) {
			entity = (Entity) obtainConverter().convertObject(object);
		}
		obtainDAOInstance().update(entity);
		postHandle(entity);
	}

	@Override
	public void deleteByPK(PK pk) throws BusinessException {
		obtainDAOInstance().deleteByPK(pk);
	}

	@Override
	public Object readDataByPK(PK pk, boolean isConvert) throws BusinessException {
		Object object = obtainDAOInstance().readDataByPK(pk);
		if (null == object) {
			throw new BusinessException("获取的对象不存在");
		}
		if (isConvert) {
			return obtainConverter().convertObject(object);
		} 
		return object;
	}

	@Override
	public Object readDataByCondition(QueryCondition queryCondition, boolean isConvert)
			throws BusinessException {
		Object object = obtainDAOInstance().readDataByCondition(queryCondition);
		if (null == object) {
			throw new BusinessException("获取的对象不存在");
		}
		if (isConvert) {
			return obtainConverter().convertObject(object);
		}
		return object;
	}
	
	@Override
	public List<?> readDataListByCondition(QueryCondition condition, boolean isConvert)
			throws BusinessException {
		List<?> queryResult = obtainDAOInstance().readDataListByCondition(condition);
		if (isConvert) {
			List<Object> resultList = new ArrayList<Object>();
			for (Object object : queryResult) {
				resultList.add(obtainConverter().convertObject(object));
			}
			return resultList;
		} 
		return queryResult;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public QueryResult<?> readDataPaginationByCondition(QueryCondition condition, boolean isConvert)
			throws BusinessException {
		QueryResult<?> queryResult = obtainDAOInstance().readDataPaginationByCondition(condition);
		if (isConvert) {
			List<Object> resultList = new ArrayList<Object>();
			for (Object object : queryResult.getResultList()) {
				resultList.add(obtainConverter().convertObject(object));
			}
			return new QueryResult(queryResult.getTotalRowNum(), resultList);
		} 
		return queryResult;
	}
	
}
