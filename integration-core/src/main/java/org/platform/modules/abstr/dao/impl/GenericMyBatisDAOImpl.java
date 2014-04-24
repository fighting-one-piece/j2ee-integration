package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("genericMyBatisDAO")
public class GenericMyBatisDAOImpl<Entity extends Serializable, PK extends Serializable>
	extends SqlSessionDaoSupport implements IGenericDAO<Entity, PK> {

	public static final String SQLID_INSERT = "insert";
    public static final String SQLID_UPDATE = "update";
    public static final String SQLID_DELETE_BY_PK = "deleteByPK";
    public static final String SQLID_READ_DATA_BY_PK = "readDataByPK";
    public static final String SQLID_READ_DATA_BY_CONDITION = "readDataByCondition";
    public static final String SQLID_READ_DATA_LIST_BY_CONDITION = "readDataListByCondition";
    public static final String SQLID_READ_DATA_PAGINATION_BY_CONDITION = "readDataPaginationByCondition";

	@Resource(name = "sqlSessionTemplate")
	protected SqlSessionTemplate sqlSessionTemplate;

	private Class<Entity> entityClass = null;

	@SuppressWarnings("unchecked")
	public GenericMyBatisDAOImpl() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
	}

	protected String obtainSQLID(String sqlId) {
		Configuration configuration = sqlSessionTemplate.getConfiguration();
		String dialect = null;
		if (null != configuration.getVariables()) {
			dialect = configuration.getVariables().getProperty("dialect");
		}
		if (null == dialect) {
			dialect = "mysql";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(dialect).append(".")
		  .append(entityClass.getName()).append(".")
		  .append(sqlId);
		return sb.toString();
	}

	@Override
	public void insert(Entity entity) {
		sqlSessionTemplate.insert(obtainSQLID(SQLID_INSERT), entity);
	}

	@Override
	public void update(Entity entity) {
		sqlSessionTemplate.update(obtainSQLID(SQLID_UPDATE), entity);
	}
	
	@Override
	public void delete(Entity entity) throws DataAccessException {
		
	}

	@Override
	public void deleteByPK(PK primaryKey) {
		sqlSessionTemplate.delete(obtainSQLID(SQLID_DELETE_BY_PK), primaryKey);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByPK(PK pk) {
		return (Entity) sqlSessionTemplate.selectOne(obtainSQLID(SQLID_READ_DATA_BY_PK), pk);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByCondition(QueryCondition condition) {
		return (Entity) sqlSessionTemplate.selectOne(obtainSQLID(SQLID_READ_DATA_BY_CONDITION), condition);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> readDataListByCondition(QueryCondition condition) throws DataAccessException {
		Map<String, Object> map = condition.getMybatisCondition();
		List<Entity> resultList = (List<Entity>) sqlSessionTemplate.selectList(
				obtainSQLID(SQLID_READ_DATA_LIST_BY_CONDITION), map);
		return null == resultList ? new ArrayList<Entity>() : resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(QueryCondition condition)
			throws DataAccessException {
		Map<String, Object> map = condition.getMybatisCondition();
		List<Entity> resultList = (List<Entity>) sqlSessionTemplate.selectList(
				obtainSQLID(SQLID_READ_DATA_PAGINATION_BY_CONDITION), map);
		int totalRowNum = (Integer) map.get(QueryCondition.TOTAL_ROW_NUM);
		return new QueryResult<Entity>(totalRowNum, resultList);
	}

	@Override
	public void flush() throws DataAccessException {
		sqlSessionTemplate.clearCache();
	}

}
