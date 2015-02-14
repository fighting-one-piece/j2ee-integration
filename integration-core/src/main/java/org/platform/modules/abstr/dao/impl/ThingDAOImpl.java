package org.platform.modules.abstr.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.dao.IThingDAO;
import org.platform.modules.abstr.entity.Thing;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("thingDAO")
public class ThingDAOImpl extends GenericMyBatisDAOImpl<Thing, Long> implements IThingDAO {
	
	public static final String UPDATE_UP = "updateUp";
    public static final String UPDATE_DOWN = "updateDown";
    public static final String UPDATE_DELETED = "updateDeleted";
    public static final String UPDATE_SPAM = "updateSpam";
    public static final String READ_UPS_BY_PK = "readUpsByPK";
    public static final String READ_DOWNS_BY_PK = "readDownsByPK";
	public static final String READ_DATA_PAGINATION_BY_CONDITION_WITH_ORDER = "readDataPaginationByConditionWithOrder";

	@Override
	public void updateUpsIncr(String table, Long id, int incr) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		map.put("incr", incr);
		sqlSessionTemplate.update(obtainSQLID(UPDATE_UP), map);
	}
	
	@Override
	public void updateDownsIncr(String table, Long id, int incr) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		map.put("incr", incr);
		sqlSessionTemplate.update(obtainSQLID(UPDATE_DOWN), map);
	}
	
	@Override
	public void updateDeleted(String table, Long id, boolean deleted) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		map.put("deleted", deleted);
		sqlSessionTemplate.update(obtainSQLID(UPDATE_DELETED), map);
	}

	@Override
	public void updateSpam(String table, Long id, boolean spam) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		map.put("spam", spam);
		sqlSessionTemplate.update(obtainSQLID(UPDATE_SPAM), map);
	}
	
	@Override
	public int readUpsByPK(String table, Long id) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		Object result = sqlSessionTemplate.selectOne(obtainSQLID(READ_UPS_BY_PK), map);
		return null == result ? 0 : (Integer) result;
	}
	
	@Override
	public int readDownsByPK(String table, Long id) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", table);
		map.put("id", id);
		Object result = sqlSessionTemplate.selectOne(obtainSQLID(READ_DOWNS_BY_PK), map);
		return null == result ? 0 : (Integer) result;
	}
	
	@Override
	public Thing readThingById(String thingTable, String dataTable, Long id) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("thingTable", thingTable);
		map.put("dataTable", dataTable);
		map.put("id", id);
		Object result = sqlSessionTemplate.selectOne(obtainSQLID(SQLID_READ_DATA_BY_PK), map);
		return null == result ? null : (Thing) result;
	}
	
	@Override
	public QueryResult<Thing> readDataPaginationByConditionWithOrder(Query query) throws DataAccessException {
//		Map<String, Object> map = query.getConditions();
//		List<Thing> things = sqlSessionTemplate.selectList(
//				obtainSQLID(READ_DATA_PAGINATION_BY_CONDITION_WITH_ORDER), map);
//		long totalRowNum = (Long) map.get(Query.TOTAL_ROW_NUM);
//		return new QueryResult<Thing>(totalRowNum, things);
		return new QueryResult<Thing>();
	}
	
}
