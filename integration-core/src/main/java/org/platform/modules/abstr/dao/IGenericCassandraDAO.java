package org.platform.modules.abstr.dao;

import java.util.List;
import java.util.Map;

import org.platform.modules.abstr.dao.cassandra.Query;
import org.platform.modules.abstr.dao.cassandra.QueryResult;
import org.platform.modules.abstr.entity.Thing;
import org.platform.modules.abstr.entity.ThingData;

public interface IGenericCassandraDAO<Entity, ID> {

	public void insert(Entity entity);
	
	public void insert(Thing thing);
	
	public void insert(ThingData thingData);
	
	public void insertThing(Entity entity);
	
	public void insertThingData(Entity entity);
	
	public void update(Entity entity);
	
	public void update(Thing thing);
	
	public void update(ThingData thingData);
	
	public void updateThing(Entity entity);
	
	public void updateThingData(Entity entity);
	
	public void deleteById(ID id);
	
	public void deleteThingById(ID id);
	
	public void deleteThingDataById(ID id);
	
	public void deleteThingDataByIdAndKey(ID id, String key);

	public Entity getOneById(ID id);
	
	public Entity getOneByParams(Map<String, Object> params);
	
	public List<Entity> getAll();
	
	public List<Entity> getAllByParams(Map<String, Object> params);
	
	public QueryResult<Entity> getAll(Query query);
	
	public int getCount(Query query);
	
}
