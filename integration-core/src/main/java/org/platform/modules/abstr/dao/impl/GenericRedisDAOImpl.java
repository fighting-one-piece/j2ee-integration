package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.platform.entity.PKEntity;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.DataAccessException;
import org.platform.utils.json.JSONUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository("genericRedisDAO")
public class GenericRedisDAOImpl <Entity extends Serializable, PK extends Serializable> implements IGenericDAO<Entity, PK> {
	
	@Resource(name = "redisTemplate")
	protected RedisTemplate<Serializable, Serializable> redisTemplate = null;
	
	/** 实体类*/
	protected Class<Entity> entityClass = null;

	@SuppressWarnings("unchecked")
	public GenericRedisDAOImpl() {
		Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<Entity>) p[0];
        }
	}

	@Override
	public void insert(final Entity entity) throws DataAccessException {
		redisTemplate.execute(new RedisCallback<Entity>() {
			@Override
			public Entity doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				connection.set(redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + ((PKEntity<?>) entity).getPK()), 
						redisTemplate.getStringSerializer().serialize(
								JSONUtils.object2json(entity)));
				return null;
			}
		});
	}

	@Override
	public void update(Entity entity) throws DataAccessException {
		
	}

	@Override
	public void delete(final Entity entity) throws DataAccessException {
		redisTemplate.execute(new RedisCallback<PK>() {
			@Override
			public PK doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				connection.del(redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + ((PKEntity<?>) entity).getPK()));
				return null;
			}
		});
	}

	@Override
	public void deleteByPK(final PK pk) throws DataAccessException {
		redisTemplate.execute(new RedisCallback<PK>() {
			@Override
			public PK doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				connection.del(redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + pk));
				return null;
			}
		});
	}

	@Override
	public Entity readDataByPK(final PK pk) throws DataAccessException {
		return redisTemplate.execute(new RedisCallback<Entity>() {
			
			@Override
			public Entity doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + pk);
				if (connection.exists(key)) {
					 String vaule = (String) redisTemplate.getValueSerializer().deserialize(
							connection.get(key));
					 JSONObject object = JSONUtils.json2Object(vaule);
					 object.toString();
					 //json数据填入对象
					 Entity entity = null;
					try {
						entity = entityClass.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					 return entity;
				}
				return null;
			}
		});
	}

	@Override
	public Entity readDataByCondition(Query query)
			throws DataAccessException {
		return null;
	}

	@Override
	public List<Entity> readDataListByCondition(Query query)
			throws DataAccessException {
		return null;
	}
	
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query)
			throws DataAccessException {
		return null;
	}

	@Override
	public void flush() throws DataAccessException {
	}

}
