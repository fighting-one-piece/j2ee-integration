package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.common.ThingUtils;
import org.platform.modules.abstr.dao.IGenericTRDAO;
import org.platform.modules.abstr.dao.IThingRelationDAO;
import org.platform.modules.abstr.entity.ThingRelation;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("genericTRDAO")
public class GenericTRDAOImpl<Entity extends Serializable, PK extends Serializable> extends GenericDAOImpl<Entity, PK> 
	implements IGenericTRDAO<Entity, PK> {

	@Resource(name = "thingRelationDAO")
	protected IThingRelationDAO thingRelationDAO = null;
	
	@SuppressWarnings("unchecked")
	public GenericTRDAOImpl() {
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
		ThingRelation thingRelation = ThingUtils.extractThingRelation(entity);
		thingRelationDAO.insert(thingRelation);
	}
	
	@Override
	public void insert(List<Entity> entities) throws DataAccessException {
		List<ThingRelation> thingRelations = new ArrayList<ThingRelation>();
		for (int i = 0, len = entities.size(); i < len; i++) {
			ThingRelation thingRelation = ThingUtils.extractThingRelation(entities.get(i));
			thingRelations.add(thingRelation);
		}
		thingRelationDAO.insert(thingRelations);
	}

	@Override
	public void update(Entity entity) throws DataAccessException {
		ThingRelation thingRelation = ThingUtils.extractThingRelationNoInfo(entity);
		fillInfo(entity, thingRelation);
		thingRelationDAO.update(thingRelation);
	}
	
	@Override
	public void update(List<Entity> entities) throws DataAccessException {
		List<ThingRelation> thingRelations = new ArrayList<ThingRelation>();
		for (int i = 0, len = entities.size(); i < len; i++) {
			Entity entity = entities.get(i);
			ThingRelation thingRelation = ThingUtils.extractThingRelationNoInfo(entity);
			fillInfo(entity, thingRelation);
			thingRelations.add(thingRelation);
		}
		thingRelationDAO.update(thingRelations);
	}
	
	@Override
	public void updateDeleted(PK pk, boolean deleted) throws DataAccessException {
		thingRelationDAO.updateDeleted(relationTable(), (Long) pk, deleted);
	}
	
	@Override
	public void updateSpam(PK pk, boolean spam) throws DataAccessException {
		thingRelationDAO.updateSpam(relationTable(), (Long) pk, spam);
	}

	@Override
	public void delete(Entity entity) throws DataAccessException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteByPK(PK pk) throws DataAccessException {
		ThingRelation thingRelation = new ThingRelation();
		thingRelation.setId((Long) pk);
		thingRelation.setTable(relationTable());
		thingRelationDAO.delete(thingRelation);
	}

	@Override
	public Entity readDataByPK(PK pk) throws DataAccessException {
		Query query = new Query();
		query.addCondition("id", pk);
		return readDataByCondition(query);
	}
	
	@Override
	public Entity readDataByThing1IdAndThing2IdAndKind(Long thing1Id,
			Long thing2Id, String kind) throws DataAccessException {
		Query query = new Query();
		query.addCondition(Query.TABLE, relationTable());
		query.addCondition("thing1Id", thing1Id);
		query.addCondition("thing2Id", thing2Id);
		query.addCondition("kind", kind);
		return readDataByCondition(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, relationTable());
		ThingRelation thingRelation = thingRelationDAO.readDataByCondition(query);
		Object entity = ThingUtils.convertThingRelationToObject(thingRelation, entityClass);
		return null != entity ? (Entity) entity : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> readDataListByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, relationTable());
		List<ThingRelation> thingRelations = thingRelationDAO.readDataListByCondition(query);
		List<Entity> entities = new ArrayList<Entity>();
		for (ThingRelation thingRelation : thingRelations) {
			Object entity = ThingUtils.convertThingRelationToObject(thingRelation, entityClass);
			entities.add((Entity) entity);
		}
		return entities;
	}
	
	@Override
	public List<Long> readThing1IdsByThing2IdAndKind(Long thing2Id, String kind) throws DataAccessException {
		List<Long> thing1Ids = new ArrayList<Long>();
		Query query = new Query();
		query.addCondition(Query.TABLE, relationTable());
		query.addCondition("thing2Id", thing2Id);
		query.addCondition("kind", kind);
		query.addCondition("deleted", false);
		query.addOrderCondition("create_time", Query.ORDER_DESC);
		List<Entity> entities = readDataListByCondition(query);
		for (Entity entity : entities) {
			Long thing1Id = (Long) ThingUtils.getValueByFieldName(entity, "thing1Id");
			if (thing1Ids.contains(thing1Id)) continue;
			thing1Ids.add(thing1Id);
		}
		return thing1Ids;
	}
	
	@Override
	public List<Long> readThing2IdsByThing1IdAndKind(Long thing1Id, String kind) throws DataAccessException {
		List<Long> thing2Ids = new ArrayList<Long>();
		Query query = new Query();
		query.addCondition(Query.TABLE, relationTable());
		query.addCondition("thing1Id", thing1Id);
		query.addCondition("kind", kind);
		query.addCondition("deleted", false);
		query.addOrderCondition("create_time", Query.ORDER_DESC);
		List<Entity> entities = readDataListByCondition(query);
		for (Entity entity : entities) {
			Long thing2Id = (Long) ThingUtils.getValueByFieldName(entity, "thing2Id");
			if (thing2Ids.contains(thing2Id)) continue;
			thing2Ids.add(thing2Id);
		}
		return thing2Ids;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, relationTable());
		query.setPagination(true);
		QueryResult<ThingRelation> qr = thingRelationDAO.readDataPaginationByCondition(query);
		List<Entity> entities = new ArrayList<Entity>();
		for (ThingRelation thingRelation : qr.getResultList()) {
			Object entity = ThingUtils.convertThingRelationToObject(thingRelation, entityClass);
			entities.add((Entity) entity);
		}
		return new QueryResult<Entity>(qr.getTotalRowNum(), entities);
	}

	@Override
	public Long readCountByCondition(Query query) throws DataAccessException {
		query.addCondition(Query.TABLE, relationTable());
		return thingRelationDAO.readCountByCondition(query);
	}

	@Override
	public void flush() throws DataAccessException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 填充ThingRelation的INFO字段
	 * @param entity
	 * @param thingRelation
	 */
	@SuppressWarnings("unchecked")
	private void fillInfo(Entity entity, ThingRelation thingRelation) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> newMap = ThingUtils.convertObjectToMap(entity);
		if (newMap.size() == 0) return;
		Object idObject = ThingUtils.getValueByFieldName(entity, "id");
		if (null != idObject) {
			ThingRelation temp = (ThingRelation) readDataByPK((PK) idObject); 
			if (null != temp){
				String info = temp.getInfo();
				Map<String, Object> oldMap = ThingUtils.convertJsonToMap(info);
				if (null != oldMap && oldMap.size() > 0) {
					map.putAll(oldMap);
				}
			}
		}
		map.putAll(newMap);
		if (map.size() == 0) return; 
		thingRelation.setInfo(ThingUtils.convertMapToJson(map));
	}
	
}
