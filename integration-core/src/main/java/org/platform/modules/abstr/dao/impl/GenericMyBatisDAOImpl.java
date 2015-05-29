package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("genericMyBatisDAO")
public class GenericMyBatisDAOImpl<Entity extends Serializable, PK extends Serializable>
	extends SqlSessionDaoSupport implements IGenericDAO<Entity, PK> {

	public static final String INSERT = "insert";
	public static final String INSERT_BATCH = "insertBatch";
    public static final String UPDATE = "update";
    public static final String UPDATE_BATCH = "updateBatch";
    public static final String DELETE_BY_PK = "deleteByPK";
    public static final String READ_DATA_BY_PK = "readDataByPK";
    public static final String READ_DATA_BY_CONDITION = "readDataByCondition";
    public static final String READ_DATA_LIST_BY_CONDITION = "readDataListByCondition";
    public static final String READ_DATA_PAGINATION_BY_CONDITION = "readDataPaginationByCondition";
    public static final String READ_COUNT_BY_CONDITION = "readCountByCondition";

	@Resource(name = "sqlSessionTemplate")
	protected SqlSessionTemplate sqlSessionTemplate = null;

	private Class<Entity> entityClass = null;

	@SuppressWarnings("unchecked")
	public GenericMyBatisDAOImpl() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
	}
	
	protected SqlSession sqlSession() {
		return sqlSessionTemplate.getSqlSessionFactory().openSession();
	}
	
	protected SqlSession batchSqlSession() {
		return sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
	}

	protected String obtainSQLID(String sqlId) {
		Configuration configuration = sqlSessionTemplate.getConfiguration();
		String dialect = "mysql";
		if (null != configuration.getVariables()) {
			dialect = configuration.getVariables().getProperty("dialect");
		}
		StringBuffer sb = new StringBuffer();
		sb.append(dialect).append(".").append(entityClass.getName()).append(".").append(sqlId);
		return sb.toString();
	}

	@Override
	public void insert(Entity entity) {
		sqlSessionTemplate.insert(obtainSQLID(INSERT), entity);
	}
	
	@Override
	public void insert(List<Entity> entities) throws DataAccessException {
		SqlSession sqlSession = batchSqlSession();
		for (int i = 0, len = entities.size(); i < len; i++) {
			sqlSession.insert(obtainSQLID(INSERT), entities.get(i));
		}
		sqlSession.flushStatements();
	}

	@Override
	public void update(Entity entity) {
		sqlSessionTemplate.update(obtainSQLID(UPDATE), entity);
	}

	@Override
	public void update(List<Entity> entities) throws DataAccessException {
		SqlSession sqlSession = batchSqlSession();
		for (int i = 0, len = entities.size(); i < len; i++) {
			sqlSession.update(obtainSQLID(UPDATE), entities.get(i));
		}
		sqlSession.flushStatements();
	}
	
	@Override
	public void delete(Entity entity) throws DataAccessException {
		
	}

	@Override
	public void deleteByPK(PK primaryKey) {
		sqlSessionTemplate.delete(obtainSQLID(DELETE_BY_PK), primaryKey);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByPK(PK pk) {
		return (Entity) sqlSession().selectOne(obtainSQLID(READ_DATA_BY_PK), pk);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByCondition(Query query) {
		Map<String, Object> map = query.getMybatisCondition();
		return (Entity) sqlSession().selectOne(obtainSQLID(READ_DATA_BY_CONDITION), map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> readDataListByCondition(Query query) throws DataAccessException {
		Map<String, Object> map = query.getMybatisCondition();
		List<Object> resultList = (List<Object>) sqlSession().selectList(
				obtainSQLID(READ_DATA_LIST_BY_CONDITION), map);
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0, len = resultList.size(); i < len; i++) {
			entities.add((Entity) resultList.get(i));
		}
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query) throws DataAccessException {
		Map<String, Object> map = query.getMybatisCondition();
		List<Object> resultList = (List<Object>) sqlSession().selectList(
				obtainSQLID(READ_DATA_PAGINATION_BY_CONDITION), map);
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0, len = resultList.size(); i < len; i++) {
			entities.add((Entity) resultList.get(i));
		}
		int totalRowNum = (Integer) map.get(Query.TOTAL_ROW_NUM);
		return new QueryResult<Entity>(totalRowNum, entities);
	}
	
	@Override
	public Long readCountByCondition(Query query) throws DataAccessException {
		Map<String, Object> map = query.getMybatisCondition();
		return  (Long) sqlSession().selectOne(obtainSQLID(READ_COUNT_BY_CONDITION), map);
	}

	@Override
	public void flush() throws DataAccessException {
		sqlSessionTemplate.clearCache();
	}

}
