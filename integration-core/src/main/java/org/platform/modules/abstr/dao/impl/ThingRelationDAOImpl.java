package org.platform.modules.abstr.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.platform.modules.abstr.dao.IThingRelationDAO;
import org.platform.modules.abstr.entity.ThingRelation;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("thingRelationDAO")
public class ThingRelationDAOImpl extends GenericMyBatisDAOImpl<ThingRelation, Long> implements IThingRelationDAO {
	
	public static final String UPDATE_DELETED = "updateDeleted";
    public static final String UPDATE_SPAM = "updateSpam";
	
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
	
}
