package org.platform.modules.abstr.biz.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.BusinessException;
import org.platform.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericBusinessImpl<Entity extends Serializable, PK extends Serializable> 
	implements IGenericBusiness<Entity, PK> {

	/** 日志*/
	protected Logger LOG = LoggerFactory.getLogger(getClass());

	protected Class<Entity> entityClass = null;
	
	@SuppressWarnings("unchecked")
	public GenericBusinessImpl() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
	}
	
	public abstract IGenericDAO<Entity, PK> obtainDAOInstance();

	protected IConverter<?,?> obtainConverter() {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected IGenericDAO<Entity, PK> obtainDefaultDAOInstance() {
		String className = getClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1, className.indexOf("BusinessImpl"));
		StringBuilder beanName = new StringBuilder();
		beanName.append(Character.toLowerCase(className.charAt(0))).append(className.substring(1)).append("DAO");
		Object daoInstance = SpringUtils.getBean(beanName.toString());
		if (null == daoInstance) throw new BusinessException("数据访问对象获取失败");
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
		Entity entity = null;
		if (entityClass.isAssignableFrom(object.getClass())) {
			entity = (Entity) object;
		} else {
			entity = (Entity) obtainConverter().convertObject(object);
		}
		obtainDAOInstance().insert(entity);
		postHandle(entity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Object object) throws BusinessException {
		preHandle(object);
		Entity entity = null;
		if (entityClass.isAssignableFrom(object.getClass())) {
			entity = (Entity) object;
		} else {
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
		if (null == object) throw new BusinessException("获取的对象不存在");
		return isConvert ? obtainConverter().convertObject(object) : object;
	}

	@Override
	public Object readDataByCondition(Query query, boolean isConvert) throws BusinessException {
		Object object = obtainDAOInstance().readDataByCondition(query);
		if (null == object) throw new BusinessException("获取的对象不存在");
		return isConvert ? obtainConverter().convertObject(object) : object;
	}
	
	@Override
	public List<?> readDataListByCondition(Query query, boolean isConvert) throws BusinessException {
		List<?> dataList = obtainDAOInstance().readDataListByCondition(query);
		if (!isConvert) return dataList;
		List<Object> resultList = new ArrayList<Object>();
		for (Object object : dataList) {
			resultList.add(obtainConverter().convertObject(object));
		}
		return resultList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public QueryResult<?> readDataPaginationByCondition(Query query, boolean isConvert) throws BusinessException {
		QueryResult<?> queryResult = obtainDAOInstance().readDataPaginationByCondition(query);
		if (!isConvert) return queryResult;
		List<Object> resultList = new ArrayList<Object>();
		for (Object object : queryResult.getResultList()) {
			resultList.add(obtainConverter().convertObject(object));
		}
		return new QueryResult(queryResult.getTotalRowNum(), resultList);
	}
	
}
