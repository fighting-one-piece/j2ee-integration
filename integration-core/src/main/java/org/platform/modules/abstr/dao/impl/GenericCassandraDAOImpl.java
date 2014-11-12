package org.platform.modules.abstr.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.platform.modules.abstr.dao.IGenericCassandraDAO;
import org.platform.modules.abstr.dao.cassandra.CQLBuilder;
import org.platform.modules.abstr.dao.cassandra.Query;
import org.platform.modules.abstr.dao.cassandra.QueryResult;
import org.platform.modules.abstr.dao.cassandra.thingdb.Thing;
import org.platform.modules.abstr.dao.cassandra.thingdb.ThingData;
import org.platform.modules.abstr.dao.cassandra.thingdb.ThingUtils;
import org.springframework.cassandra.core.QueryOptions;
import org.springframework.cassandra.core.RetryPolicy;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.core.querybuilder.Update.Assignments;

@Repository("genericCassandraDAO")
public class GenericCassandraDAOImpl<Entity, ID> implements IGenericCassandraDAO<Entity, ID> {
	
	private static Logger LOG = Logger.getLogger(GenericCassandraDAOImpl.class);
	
	private Class<Entity> entityClass = null;
	
	@Resource(name = "cassandraTemplate")
	private CassandraTemplate cassandraTemplate = null;
	
	@SuppressWarnings("unchecked")
	public GenericCassandraDAOImpl() {
		Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
	}
	
	public CassandraTemplate getCassandraTemplate() {
		return cassandraTemplate;
	}
	
	public Session getSession () {
		return cassandraTemplate.getSession();
	}
	
	public String thingTable() {
		return ThingUtils.thingTable(entityClass);
	}
	
	public String dataTable() {
		return ThingUtils.dataTable(entityClass);
	}
	
	public Entity newEntityInstance() {
		Entity entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return entity;
	}

	@Override
	public void insert(Entity entity) {
		insertThing(entity);
		insertThingData(entity);
	}
	
	@Override
	public void insert(Thing thing) {
		Insert insert = QueryBuilder.insertInto(thingTable());
		insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
		Map<String, Object> map = ThingUtils.convertObjectToMap(thing, true);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			insert.value(entry.getKey(), entry.getValue());
		}
		cassandraTemplate.execute(insert);
	}

	@Override
	public void insert(ThingData thingData) {
		Insert insert = QueryBuilder.insertInto(dataTable());
		insert.setConsistencyLevel(ConsistencyLevel.QUORUM);
		Map<String, Object> map = ThingUtils.convertObjectToMap(thingData, true);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			insert.value(entry.getKey(), entry.getValue());
		}
		cassandraTemplate.execute(insert);
	}
	
	@Override
	public void insertThing(Entity entity) {
		insert(ThingUtils.extractThing(entity));
	}
	
	@Override
	public void insertThingData(Entity entity) {
		List<ThingData> thingDatas = ThingUtils.extractThingDatas(entity);
		for (ThingData thingData : thingDatas) {
			insert(thingData);
		}
	}
	
	@Override
	public void update(Entity entity) {
		updateThing(entity);
		updateThingData(entity);
	}
	
	@Override
	public void update(Thing thing) {
		Update update = QueryBuilder.update(thingTable());
		update.setConsistencyLevel(ConsistencyLevel.QUORUM);
		Map<String, Object> map = ThingUtils.convertObjectToMap(thing, true);
		String id = (String) map.get("id");
		map.remove("id");
		Assignments assignments = update.with();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			assignments.and(QueryBuilder.set(entry.getKey(), entry.getValue()));
		}
		update.where(QueryBuilder.eq("id", id));
		cassandraTemplate.execute(update);
	}
	
	@Override
	public void update(ThingData thingData) {
		Update update = QueryBuilder.update(dataTable());
		update.setConsistencyLevel(ConsistencyLevel.QUORUM);
		Assignments assignments = update.with();
		assignments.and(QueryBuilder.set("value", thingData.getValue()));
		assignments.and(QueryBuilder.set("kind", thingData.getKind()));
		com.datastax.driver.core.querybuilder.Update.Where where = update.where();
		where.and(QueryBuilder.eq("thing_id", thingData.getThingId()));
		where.and(QueryBuilder.eq("key", thingData.getKey()));
		cassandraTemplate.execute(update);
	}
	
	@Override
	public void updateThing(Entity entity) {
		update(ThingUtils.extractThing(entity));
	}
	
	@Override
	public void updateThingData(Entity entity) {
		List<ThingData> thingDatas = ThingUtils.extractThingDatas(entity);
		for (ThingData thingData : thingDatas) {
			update(thingData);
		}
	}
	
	@Override
	public void deleteById(ID id) {
		deleteThingById(id);
	}
	
	@Override
	public void deleteThingById(ID id) {
		deleteThingDataById(id);
		Delete delete = QueryBuilder.delete().from(thingTable());
		delete.setConsistencyLevel(ConsistencyLevel.ALL);
		delete.where(QueryBuilder.eq("id", id));
		cassandraTemplate.execute(delete);
	}
	
	@Override
	public void deleteThingDataById(ID id) {
		Delete delete = QueryBuilder.delete().from(dataTable());
		delete.setConsistencyLevel(ConsistencyLevel.ALL);
		delete.where(QueryBuilder.eq("thing_id", id));
		cassandraTemplate.execute(delete);
	}
	
	@Override
	public void deleteThingDataByIdAndKey(ID id, String key) {
		Delete delete = QueryBuilder.delete().from(dataTable());
		delete.setConsistencyLevel(ConsistencyLevel.ALL);
		delete.where().and(QueryBuilder.eq("thing_id", id))
			.and(QueryBuilder.eq("key", key));
		cassandraTemplate.execute(delete);
	}
	
	@Override
	public Entity getOneById(ID id) {
		String tCQL = "select * from " + thingTable() + " where id = ?";
		String dCQL = "select * from " + dataTable() + " where thing_id = ?";
		List<Entity> entities = getAll(tCQL, new Object[]{id} , dCQL, new Object[]{id});
		return entities.size() == 1 ? entities.get(0) : null;
	}
	
	@Override
	public Entity getOneByParams(Map<String, Object> params) {
		List<Entity> entities = getAllByParams(params);
		return entities.size() == 1 ? entities.get(0) : null;
	}
	
	@Override
	public List<Entity> getAll() {
		String tCQL = "select * from " + thingTable();
		String dCQL = "select * from " + dataTable() + " where thing_id = ?";
		return getAll(tCQL, null, dCQL, null);
	}
	
	@Override
	public List<Entity> getAllByParams(Map<String, Object> params) {
		Map<String, Object> tAttris = new HashMap<String, Object>();
		Map<String, Object> dAttris = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String attribute = entry.getKey();
			Object value = entry.getValue();
			if (ThingUtils.isBasicAttribute(attribute)) {
				tAttris.put(attribute, value);
			} else {
				dAttris.put(attribute, value);
			}
		}
		int tAttrisLen = tAttris.size();
		int dAttrisLen = dAttris.size();
		if (dAttrisLen > 1) {
			LOG.error("暂只支持一个数据属性查询");
		}
		if (tAttrisLen > 0 && dAttrisLen == 0) {
			return getAllByThingParams(tAttris);
		} else if (tAttrisLen == 0 && dAttrisLen > 0) {
			return getAllByDataParams(dAttris);
		} else if (tAttrisLen > 0 && dAttrisLen > 0) {
			return getAllByThingAndDataParams(tAttris, dAttris);
		}
		return getAll();
	}
	
	@Override
	public QueryResult<Entity> getAll(Query query) {
		int tAttrisLen = query.getBasicAttributesLength();
		int dAttrisLen = query.getDataAttributesLength();
		QueryResult<Entity> results = new QueryResult<Entity>();
		if (tAttrisLen > 0 && dAttrisLen == 0) {
			results = getAllByThingParams(query);
		} else if (tAttrisLen == 0 && dAttrisLen > 0) {
			results =  getAllByDataParams(query);
		} else if (tAttrisLen > 0 && dAttrisLen > 0) {
			results = getAllByThingAndDataParams(query);
		} else {
			List<Entity> entities = getAll();
			results.setResultList(entities);
			results.setTotalRowNum(entities.size());
		}
		return results;
	}
	
	@Override
	public int getCount(Query query) {
		Long count = null;
		String tCountCQL = CQLBuilder.buildThingCountCQL(entityClass, query);
		QueryOptions options = new QueryOptions();
		options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
		options.setRetryPolicy(RetryPolicy.DEFAULT);
		ResultSet resultSet = cassandraTemplate.query(tCountCQL, options);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			count = row.getLong(0);
		}
		return null == count ? 0 : count.intValue();
	}
	
	private List<Entity> getAll(String tCQL, Object[] tParams, String dCQL, Object[] dParams) {
		List<Entity> entities = new ArrayList<Entity>();
		ResultSet tResultSet = null;
		if (null == tParams || tParams.length == 0) {
			tResultSet = getSession().execute(tCQL);
		} else {
			tResultSet = getSession().execute(tCQL, tParams);
		}
		Iterator<Row> iterator = tResultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			Object[] params = null;
			if (null == dParams || dParams.length == 0) {
				params = new Object[]{row.getLong("id")};
			} else {
				params = dParams;
			}
			ResultSet dResultSet = getSession().execute(dCQL, params);
			resultSetToEntity(dResultSet, entity);
			entities.add(entity);
		}
		return entities;
	}
	
	private List<Entity> getAllByThingParams(Map<String, Object> params) {
		List<Entity> entities = new ArrayList<Entity>();
		Object[] tCQL = CQLBuilder.buildThingCQLAndParams(entityClass, params);
		ResultSet resultSet = getSession().execute(String.valueOf(tCQL[0]), (Object[]) tCQL[1]);
		String dCQL = "select * from " + dataTable() + " where thing_id = ?";
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			ResultSet dResultSet = getSession().execute(dCQL, new Object[]{row.getLong("id")});
			resultSetToEntity(dResultSet, entity);
			entities.add(entity);
		}
		return entities;
	}
	
	private QueryResult<Entity> getAllByThingParams(Query query) {
		List<Entity> entities = new ArrayList<Entity>();
		String tCQL = CQLBuilder.buildThingCQL(entityClass, query);
		QueryOptions options = new QueryOptions();
		options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
		options.setRetryPolicy(RetryPolicy.DEFAULT);
		ResultSet resultSet = cassandraTemplate.query(tCQL, options);
		StringBuilder dCQLPrefix = new StringBuilder();
		dCQLPrefix.append("select * from ").append(dataTable()).append(" where thing_id = ");
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			StringBuilder dCQL = new StringBuilder(dCQLPrefix);
			dCQL.append(row.getLong("id"));
			ResultSet dResultSet = cassandraTemplate.query(dCQL.toString(), options);
			resultSetToEntity(dResultSet, entity);
			entities.add(entity);
		}
		return new QueryResult<Entity>(getCount(query), entities);
	}
	
	private List<Entity> getAllByThingAndDataParams(Map<String, Object> tParams, Map<String, Object> dParams) {
		List<Entity> entities = new ArrayList<Entity>();
		Object[] tCQL = CQLBuilder.buildThingCQLAndParams(entityClass, tParams);
		ResultSet resultSet = getSession().execute(String.valueOf(tCQL[0]), (Object[]) tCQL[1]);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			dParams.put("thingId", row.getLong("id"));
			Object[] dCQL = CQLBuilder.buildDataCQLAndParams(entityClass, dParams);
			ResultSet dResultSet = getSession().execute(String.valueOf(dCQL[0]), (Object[]) dCQL[1]);
			if (dResultSet.all().size() == 0) continue;
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			String cql = "select * from " + dataTable() + " where thing_id = ?";
			ResultSet fResultSet = getSession().execute(cql, new Object[]{row.getLong("id")});
			resultSetToEntity(fResultSet, entity);
			entities.add(entity);
		}
		return entities;
	}
	
	@SuppressWarnings("unused")
	private QueryResult<Entity> getAllByThingAndOneDataParams(Query query) {
		List<Entity> entities = new ArrayList<Entity>();
		String tCQL = CQLBuilder.buildThingCQL(entityClass, query);
		QueryOptions options = new QueryOptions();
		options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
		options.setRetryPolicy(RetryPolicy.DEFAULT);
		ResultSet resultSet = cassandraTemplate.query(tCQL, options);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			query.addCondition("thingId", row.getLong("id"));
			String dCQL = CQLBuilder.buildOneConditionDataCQL(entityClass, query);
			ResultSet dResultSet = cassandraTemplate.query(dCQL, options);
			if (dResultSet.all().size() == 0) continue;
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			String cql = "select * from " + dataTable() + " where thing_id = ?";
			ResultSet fResultSet = getSession().execute(cql, new Object[]{row.getLong("id")});
			resultSetToEntity(fResultSet, entity);
			entities.add(entity);
		}
		return new QueryResult<Entity>(entities.size(), entities);
	}
	
	private QueryResult<Entity> getAllByThingAndDataParams(Query query) {
		List<Entity> entities = new ArrayList<Entity>();
		String tCQL = CQLBuilder.buildThingCQL(entityClass, query);
		QueryOptions options = new QueryOptions();
		options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
		options.setRetryPolicy(RetryPolicy.DEFAULT);
		ResultSet resultSet = cassandraTemplate.query(tCQL, options);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			query.addCondition("thingId", row.getLong("id"));
			List<String> cqls = CQLBuilder.buildMultiConditionDataCQL(entityClass, query);
			int hitCount = 0;
			for (String cql : cqls) {
				ResultSet dResultSet = cassandraTemplate.query(cql, options);
				if (dResultSet.all().size() == 0) continue;
				hitCount += 1;
			}
			if (cqls.size() != hitCount) continue;
			Entity entity = newEntityInstance();
			rowToEntityCommonProps(row, entity);
			String cql = "select * from " + dataTable() + " where thing_id = ?";
			ResultSet fResultSet = getSession().execute(cql, new Object[]{row.getLong("id")});
			resultSetToEntity(fResultSet, entity);
			entities.add(entity);
		}
		return new QueryResult<Entity>(entities.size(), entities);
	}
	
	@SuppressWarnings("unchecked")
	private List<Entity> getAllByDataParams(Map<String, Object> params) {
		List<Entity> entities = new ArrayList<Entity>();
		Set<ID> ids = new HashSet<ID>();
		Object[] dCQL = CQLBuilder.buildDataCQLAndParams(entityClass, params);
		ResultSet resultSet = getSession().execute(String.valueOf(dCQL[0]), (Object[]) dCQL[1]);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			ID id = (ID) new Long(row.getLong("thing_id"));
			if (ids.contains(id)) continue;
			ids.add(id);
			Entity entity = getOneById(id);
			if (null != entity) entities.add(entity);
		}
		return entities;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private QueryResult<Entity> getAllByOneDataParams(Query query) {
		Set<ID> ids = new HashSet<ID>();
		String dCQL = CQLBuilder.buildOneConditionDataCQL(entityClass, query);
		QueryOptions options = new QueryOptions();
		options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
		options.setRetryPolicy(RetryPolicy.DEFAULT);
		ResultSet resultSet = cassandraTemplate.query(dCQL, options);
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			ID id = (ID) new Long(row.getLong("thing_id"));
			if (ids.contains(id)) continue;
			ids.add(id);
		}
		List<Entity> entities = new ArrayList<Entity>();
		for (ID id : ids) {
			Entity entity = getOneById(id);
			if (null != entity) entities.add(entity);
		}
		return new QueryResult<Entity>(entities.size(), entities);
	}
	
	@SuppressWarnings("unchecked")
	private QueryResult<Entity> getAllByDataParams(Query query) {
		List<String> cqls = CQLBuilder.buildMultiConditionDataCQL(entityClass, query);
		List<Set<ID>> idList = new ArrayList<Set<ID>>();
		for (String cql : cqls) {
			Set<ID> ids = new HashSet<ID>();
			QueryOptions options = new QueryOptions();
			options.setConsistencyLevel(org.springframework.cassandra.core.ConsistencyLevel.ONE);
			options.setRetryPolicy(RetryPolicy.DEFAULT);
			ResultSet resultSet = cassandraTemplate.query(cql, options);
			Iterator<Row> iterator = resultSet.iterator();
			while (iterator.hasNext()) {
				Row row = iterator.next();
				ID id = (ID) new Long(row.getLong("thing_id"));
				if (ids.contains(id)) continue;
				ids.add(id);
			}
			idList.add(ids);
		}
		Set<ID> ids = new HashSet<ID>();
		if (idList.size() > 0) {
			for (ID id : idList.get(0)) {
				boolean flag = true;
				for (Set<ID> tmpIds : idList) {
					if (!tmpIds.contains(id)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					ids.add(id);
				}
			}
		}
		List<Entity> entities = new ArrayList<Entity>();
		for (ID id : ids) {
			Entity entity = getOneById(id);
			if (null != entity) entities.add(entity);
		}
		return new QueryResult<Entity>(entities.size(), entities);
	}
	
	protected void rowToEntityCommonProps(Row row, Entity entity) {
		ThingUtils.setValueBySetMethod(entity, row.getLong("id"), "setId", Long.class);
		ThingUtils.setValueBySetMethod(entity, row.getInt("ups"), "setUps", Integer.class);
		ThingUtils.setValueBySetMethod(entity, row.getInt("downs"), "setDowns", Integer.class);
		ThingUtils.setValueBySetMethod(entity, row.getBool("deleted"), "setDeleted", Boolean.class);
		ThingUtils.setValueBySetMethod(entity, row.getBool("spam"), "setSpam", Boolean.class);
		ThingUtils.setValueBySetMethod(entity, row.getDate("date"), "setDate", Date.class);
	}
	
	protected void resultSetToEntity(ResultSet resultSet, Entity entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<Row> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			String kind = row.getString("kind");
			String value = row.getString("value");
			map.put(row.getString("key"), ThingUtils.getValueByKind(kind, value));
		}
		if (map.size() == 0) return;
		ThingUtils.convertMapToObject(map, entity);
	}
	
}

