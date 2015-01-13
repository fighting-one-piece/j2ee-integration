package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.common.Kind;
import org.platform.modules.abstr.common.ThingUtils;
import org.platform.modules.abstr.dao.IGenericTSDAO;
import org.platform.modules.abstr.dao.IThingDAO;
import org.platform.modules.abstr.dao.IThingDataDAO;
import org.platform.modules.abstr.entity.Thing;
import org.platform.modules.abstr.entity.ThingData;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("genericTSDAO")
public class GenericTSDAOImpl<Entity extends Serializable, PK extends Serializable> extends GenericDAOImpl<Entity, PK> 
	implements IGenericTSDAO<Entity, PK> {

	@Resource(name = "thingDAO")
	protected IThingDAO thingDAO = null;
	
	@Resource(name = "thingDataDAO")
	protected IThingDataDAO thingDataDAO = null;
	
	@SuppressWarnings("unchecked")
	public GenericTSDAOImpl() {
        Type type = getClass().getGenericSuperclass();
    	if (type instanceof ParameterizedType) {
    		Type t = ((ParameterizedType) type).getActualTypeArguments()[0];
    		if (Class.class.isAssignableFrom(t.getClass())) {
    			entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
    		}
    	}
	}

	@Override
	public void insert(Entity entity) throws DataAccessException {
		Thing thing = ThingUtils.extractThing(entity);
		insert(thing);
		List<ThingData> thingDatas = ThingUtils.extractThingDatas(entity);
		for (ThingData thingData : thingDatas) {
			thingData.setThingId(thing.getId());
		}
		thingDataDAO.insert(thingDatas);
		ThingUtils.setValueByFieldName(entity, "id", thing.getId());
	}
	
	public void insert(Thing thing) throws DataAccessException {
		thing.setTable(thingTable());
		thingDAO.insert(thing);
	}
	
	public void insert(ThingData thingData) throws DataAccessException {
		thingData.setTable(dataTable());
		thingDataDAO.insert(thingData);
	}
	
	@Override
	public void insert(List<Entity> entities) throws DataAccessException {
		List<ThingData> thingDatas = new ArrayList<ThingData>();
		for (int i = 0, len = entities.size(); i < len; i++) {
			Entity entity = entities.get(i);
			Thing thing = ThingUtils.extractThing(entity);
			thingDAO.insert(thing);
			List<ThingData> temp = ThingUtils.extractThingDatas(entity);
			for (ThingData thingData : temp) {
				thingData.setThingId(thing.getId());
				thingDatas.add(thingData);
			}
			
		}
		thingDataDAO.insert(thingDatas);
	}
	
	@Override
	public void update(Entity entity) throws DataAccessException {
		Thing thing = ThingUtils.extractThing(entity);
		if (ThingUtils.isNeedUpdate(thing)) update(thing);
		List<ThingData> thingDatas = ThingUtils.extractThingDatas(entity);
		if (thingDatas.size() == 0) return;
		Query query = new Query();
		query.addCondition(Query.TABLE, dataTable());
		query.addCondition("thingId", thing.getId());
		List<ThingData> existThingDatas = thingDataDAO.readDataListByCondition(query);
		Set<String> attributes = new HashSet<String>();
		for (ThingData existThingData : existThingDatas) {
			attributes.add(existThingData.getAttribute());
		}
		List<ThingData> insertThingDatas = new ArrayList<ThingData>();
		List<ThingData> updateThingDatas = new ArrayList<ThingData>();
		for (ThingData thingData : thingDatas) {
			if (attributes.contains(thingData.getAttribute())) {
				updateThingDatas.add(thingData);
			} else {
				insertThingDatas.add(thingData);
			}
		}
		if (insertThingDatas.size() > 0) {
			thingDataDAO.insert(insertThingDatas);
		}
		if (updateThingDatas.size() > 0) {
			thingDataDAO.update(updateThingDatas);
		}
	}
	
	public void update(Thing thing) throws DataAccessException {
		thing.setTable(thingTable());
		thingDAO.update(thing);
	}
	
	public void update(ThingData thingData) throws DataAccessException {
		thingData.setTable(dataTable());
		thingDataDAO.update(thingData);
	}
	
	@Override
	public void update(List<Entity> entities) throws DataAccessException {
		List<Thing> things = new ArrayList<Thing>();
		List<ThingData> insertThingDatas = new ArrayList<ThingData>();
		List<ThingData> updateThingDatas = new ArrayList<ThingData>();
		for (int i = 0, len = entities.size(); i < len; i++) {
			Entity entity = entities.get(i);
			Thing thing = ThingUtils.extractThing(entity);
			if (ThingUtils.isNeedUpdate(thing)) things.add(thing);
			List<ThingData> temp = ThingUtils.extractThingDatas(entity);
			if (temp.size() == 0) return;
			Query query = new Query();
			query.addCondition(Query.TABLE, dataTable());
			query.addCondition("thingId", thing.getId());
			List<ThingData> existThingDatas = thingDataDAO.readDataListByCondition(query);
			Set<String> attributes = new HashSet<String>();
			for (ThingData existThingData : existThingDatas) {
				attributes.add(existThingData.getAttribute());
			}
			for (ThingData thingData : temp) {
				if (attributes.contains(thingData.getAttribute())) {
					updateThingDatas.add(thingData);
				} else {
					insertThingDatas.add(thingData);
				}
			}
		}
		thingDAO.update(things);
		if (insertThingDatas.size() > 0) {
			thingDataDAO.insert(insertThingDatas);
		}
		if (updateThingDatas.size() > 0) {
			thingDataDAO.update(updateThingDatas);
		}
	}
	
	@Override
	public void updateUpIncr(PK pk, int incr) throws DataAccessException {
		thingDAO.updateUpIncr(thingTable(), (Long) pk, incr);
	}
	
	@Override
	public void updateDownIncr(PK pk, int incr) throws DataAccessException {
		thingDAO.updateDownIncr(thingTable(), (Long) pk, incr);
	}
	
	@Override
	public void updateDeleted(PK pk, boolean deleted) throws DataAccessException {
		thingDAO.updateDeleted(thingTable(), (Long) pk, deleted);
	}
	
	@Override
	public void updateSpam(PK pk, boolean spam) throws DataAccessException {
		thingDAO.updateSpam(thingTable(), (Long) pk, spam);
	}
	
	@Override
	public void updateIntAttributeIncr(PK pk, String attribute, int incr) throws DataAccessException {
		ThingData thingData = readThingDataByThingIdAndAttribute((Long) pk, attribute);
		if (null == thingData) thingData = new ThingData();
		String value = thingData.getValue();
		int num = null == value ? incr : Integer.parseInt(value) + incr;
		thingData.setValue(String.valueOf(num));
		if (null == thingData.getThingId()) {
			thingData.setThingId((Long) pk);
			thingData.setAttribute(attribute);
			thingData.setKind(Kind.INTEGER.getName());
			insert(thingData);
		} else {
			update(thingData);
		}
	}
	
	@Override
	public void delete(Entity entity) throws DataAccessException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteByPK(PK pk) throws DataAccessException {
		deleteThingByPK(pk);
		deleteThingDataByPK(pk);
	}
	
	public void deleteThingByPK(PK pk) throws DataAccessException {
		Thing thing = new Thing();
		thing.setTable(thingTable());
		thing.setId((Long) pk);
		thingDAO.delete(thing);
	}
	
	public void deleteThingDataByPK(PK pk) throws DataAccessException {
		ThingData thingData = new ThingData();
		thingData.setTable(dataTable());
		thingData.setThingId((Long) pk);
		thingDataDAO.delete(thingData);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByPK(PK pk) throws DataAccessException {
		Thing thing = thingDAO.readThingById(thingTable(), dataTable(), (Long) pk);
		Object entity = ThingUtils.convertThingToObject(thing, entityClass);
		return null != entity ? (Entity) entity : null;
	}
	
	public Thing readThingById(Long id) throws DataAccessException {
		return thingDAO.readThingById(thingTable(), dataTable(), id);
	}
	
	public ThingData readThingDataByThingIdAndAttribute(Long thingId, String attribute) throws DataAccessException {
		Query query = new Query();
		query.addCondition(Query.TABLE, dataTable());
		query.addCondition("thingId", thingId);
		query.addCondition("attribute", attribute);
		return thingDataDAO.readDataByCondition(query);
	}
	
	public List<ThingData> readThingDatasByThingId(Long thingId) throws DataAccessException {
		Query query = new Query();
		query.addCondition(Query.TABLE, dataTable());
		query.addCondition("thingId", thingId);
		return thingDataDAO.readDataListByCondition(query);
	}
	
	@Override
	public int readUpsByPK(Long id) throws DataAccessException {
		return thingDAO.readUpsByPK(thingTable(), id);
	}
	
	@Override
	public int readDownsByPK(Long id) throws DataAccessException {
		return thingDAO.readDownsByPK(thingTable(), id);
	}
	
	@Override
	public Entity readDataByCondition(Query query) throws DataAccessException {
		List<Entity> entities = readDataListByCondition(query);
		return entities.size() == 1 ? entities.get(0) : null;
	}
	
	public Thing readThingByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.T_TABLE, thingTable());
		query.addCondition(Query.D_TABLE, dataTable());
		return thingDAO.readDataByCondition(query);
	}
	
	public ThingData readThingDataByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, dataTable());
		return thingDataDAO.readDataByCondition(query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> readDataListByCondition(Query query) throws DataAccessException {
		Map<String, Object> dataAttributes = query.getDataAttributes();
		List<Set<Long>> idList = new ArrayList<Set<Long>>();
		for (Map.Entry<String, Object> entry : dataAttributes.entrySet()) {
			String attribute = entry.getKey();
			String value = String.valueOf(entry.getValue());
			Query thingDataQuery = genThingDataQuery(attribute, value);
			List<ThingData> thingDatas = thingDataDAO.readDataListByCondition(thingDataQuery);
			Set<Long> thingIds = new HashSet<Long>();
			for (ThingData thingData : thingDatas) {
				Long thingId = thingData.getThingId();
				if (thingIds.contains(thingId)) continue;
				thingIds.add(thingId);
			}
			if (thingIds.size() > 0) idList.add(thingIds);
		}
		if ((dataAttributes.size() > 0 && idList.size() == 0) ||
				dataAttributes.size() != idList.size()) {
			return new ArrayList<Entity>();
		}
		Set<Long> ids = new HashSet<Long>();
		if (idList.size() > 0) {
			for (Long id : idList.get(0)) {
				boolean flag = true;
				for (Set<Long> tmpIds : idList) {
					if (!tmpIds.contains(id)) {
						flag = false;
						break;
					}
				}
				if (flag) ids.add(id);
			}
		}
		if (ids.size() > 0) {
			query.addCondition("ids", new ArrayList<Long>(ids));
		}
		List<Thing> things = readThingsByCondition(query);
		boolean isRead = null == query.getDataOrderAttribute() ? false : true;
		List<Entity> entities = new ArrayList<Entity>();
		for (Thing thing : things) {
			if (isRead) thing = thingDAO.readThingById(thingTable(), dataTable(), thing.getId());
			Object entity = ThingUtils.convertThingToObject(thing, entityClass);
			entities.add((Entity) entity);
		}
		return entities;
	}
	
	public List<Thing> readThingsByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.T_TABLE, thingTable());
		query.addCondition(Query.D_TABLE, dataTable());
		return thingDAO.readDataListByCondition(query);
	}
	
	public List<ThingData> readThingDatasByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, dataTable());
		return thingDataDAO.readDataListByCondition(query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query) throws DataAccessException {
		Map<String, Object> dataAttributes = query.getDataAttributes();
		List<Set<Long>> idList = new ArrayList<Set<Long>>();
		for (Map.Entry<String, Object> entry : dataAttributes.entrySet()) {
			String attribute = entry.getKey();
			String value = String.valueOf(entry.getValue());
			Query thingDataQuery = genThingDataQuery(attribute, value);
			List<ThingData> thingDatas = thingDataDAO.readDataListByCondition(thingDataQuery);
			Set<Long> thingIds = new HashSet<Long>();
			for (ThingData thingData : thingDatas) {
				Long thingId = thingData.getThingId();
				if (thingIds.contains(thingId)) continue;
				thingIds.add(thingId);
			}
			if (thingIds.size() > 0) idList.add(thingIds);
		}
		if ((dataAttributes.size() > 0 && idList.size() == 0) ||
				dataAttributes.size() != idList.size()) {
			return new QueryResult<Entity>();
		}
		Set<Long> ids = new HashSet<Long>();
		if (idList.size() > 0) {
			for (Long id : idList.get(0)) {
				boolean flag = true;
				for (Set<Long> tmpIds : idList) {
					if (!tmpIds.contains(id)) {
						flag = false;
						break;
					}
				}
				if (flag) ids.add(id);
			}
		}
		if (ids.size() > 0) {
			query.addCondition("ids", new ArrayList<Long>(ids));
		}
		query.setPagination(true);
		QueryResult<Thing> qr = new QueryResult<Thing>();
		if (null == query.getDataOrderAttribute()) {
			query.addCondition(Query.TABLE, thingTable());
			qr = thingDAO.readDataPaginationByCondition(query);
		} else {
			query.addCondition(Query.T_TABLE, thingTable());
			query.addCondition(Query.D_TABLE, dataTable());
			qr = thingDAO.readDataPaginationByConditionWithOrder(query);
		}
		List<Entity> entities = new ArrayList<Entity>();
		for (Thing thing : qr.getResultList()) {
			Thing t = thingDAO.readThingById(thingTable(), dataTable(), thing.getId());
			Object entity = ThingUtils.convertThingToObject(t, entityClass);
			entities.add((Entity) entity);
		}
		return new QueryResult<Entity>(qr.getTotalRowNum(), entities);
	}
	
	@Override
	public Long readCountByCondition(Query query) throws DataAccessException {
		List<Entity> entities = readDataListByCondition(query);
		return (long) entities.size();
	}
	
	@Override
	public void flush() throws DataAccessException {
		throw new UnsupportedOperationException();
	}
	
	private Query genThingDataQuery(String attribute, String value) {
		Query query = new Query();
		query.addCondition(Query.TABLE, dataTable());
		if (attribute.contains("Like")) {
			attribute = attribute.substring(0, attribute.indexOf("Like"));
			query.addCondition("valueLike", value);
		} else if (attribute.contains("GT")) {
			attribute = attribute.substring(0, attribute.indexOf("GT"));
			query.addCondition("valueGT", value);
		} else if (attribute.contains("GE")) {
			attribute = attribute.substring(0, attribute.indexOf("GE"));
			query.addCondition("valueGE", value);
		} else if (attribute.contains("LT")) {
			attribute = attribute.substring(0, attribute.indexOf("LT"));
			query.addCondition("valueLT", value);
		} else if (attribute.contains("LE")) {
			attribute = attribute.substring(0, attribute.indexOf("LE"));
			query.addCondition("valueLE", value);
		} else {
			query.addCondition("value", value);
		}
		query.addCondition("attribute", attribute);
		return query;
	}
	
}
