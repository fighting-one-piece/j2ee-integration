package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;

@Repository("genericMongoDAO")
public class GenericMongoDAOImpl <Entity extends Serializable, PK extends Serializable> implements IGenericDAO<Entity, PK> {
	
	@Resource(name = "mongoTemplate")
	protected MongoTemplate mongoTemplate = null;
	
	/** 实体类*/
	protected Class<Entity> entityClass = null;

	@SuppressWarnings("unchecked")
	public GenericMongoDAOImpl() {
		Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<Entity>) p[0];
        }
	}

	@Override
	public void insert(Entity entity) throws DataAccessException {
		mongoTemplate.insert(entity);
	}

	@Override
	public void update(Entity entity) throws DataAccessException {
		//mongoTemplate.upsert(query, update, entityClass);
	}

	@Override
	public void delete(Entity entity) throws DataAccessException {
		
	}

	@Override
	public void deleteByPK(final PK pk) throws DataAccessException {
		
	}

	@Override
	public Entity readDataByPK(PK pk) throws DataAccessException {
		return mongoTemplate.findById(pk, entityClass);
	}

	@Override
	public Entity readDataByCondition(Query query) throws DataAccessException {
		return null;
	}
	
	@Override
	public List<Entity> readDataListByCondition(Query query)
			throws DataAccessException {
		List<Entity> resultList = mongoTemplate.findAll(entityClass);
		return null == resultList ? new ArrayList<Entity>() : resultList;
	}

	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query)
			throws DataAccessException {
		List<Entity> resultList = mongoTemplate.findAll(entityClass);
		return new QueryResult<>(resultList.size(), resultList);
	}

	@Override
	public void flush() throws DataAccessException {
	}

}
