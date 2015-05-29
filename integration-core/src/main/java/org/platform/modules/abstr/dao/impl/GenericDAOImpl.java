package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.platform.modules.abstr.common.ThingUtils;
import org.springframework.stereotype.Repository;

@Repository("genericDAO")
public class GenericDAOImpl<Entity extends Serializable, PK extends Serializable> extends SqlSessionDaoSupport {

	public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String UPDATE_UP = "updateUp";
    public static final String UPDATE_DOWN = "updateDown";
    public static final String UPDATE_DELETED = "updateDeleted";
    public static final String UPDATE_SPAM = "updateSpam";
    public static final String DELETE = "delete";
    public static final String DELETE_BY_PK = "deleteByPK";
    public static final String READ_DATA_BY_PK = "readDataByPK";
    public static final String READ_UPS_BY_PK = "readUpsByPK";
    public static final String READ_DOWNS_BY_PK = "readDownsByPK";
    public static final String READ_DATA_BY_CONDITION = "readDataByCondition";
    public static final String READ_DATA_LIST_BY_CONDITION = "readDataListByCondition";
    public static final String READ_DATA_PAGINATION_BY_CONDITION = "readDataPaginationByCondition";
    public static final String READ_COUNT_BY_CONDITION = "readCountByCondition";
    
	@Resource(name = "sqlSessionTemplate")
	protected SqlSessionTemplate sqlSessionTemplate = null;
	
	protected Class<Entity> entityClass = null;
	
	protected void setEntityClass(Class<Entity> entityClass) {
		this.entityClass = entityClass;
	}
	
	protected Entity newEntity() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("can not instantiated entity : " + this.entityClass, e);
        }
    }
	
	protected SqlSession sqlSession() {
		return sqlSessionTemplate.getSqlSessionFactory().openSession();
	}
	
	protected SqlSession batchSqlSession() {
		return sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
	}
	
	protected String thingTable() {
		return ThingUtils.thingTable(entityClass);
	}
	
	protected String dataTable() {
		return ThingUtils.dataTable(entityClass);
	}
	
	protected String relationTable() {
		return ThingUtils.relationTable(entityClass);
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
		StringBuilder sb = new StringBuilder();
		sb.append(dialect).append(".")
		  .append(entityClass.getName()).append(".")
		  .append(sqlId);
		return sb.toString();
	}
	
}
