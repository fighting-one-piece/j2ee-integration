package org.platform.modules.abstr.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.platform.entity.Query;
import org.platform.modules.abstr.dao.IThingDataDAO;
import org.platform.modules.abstr.entity.ThingData;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("thingDataDAO")
public class ThingDataDAOImpl extends GenericMyBatisDAOImpl<ThingData, Long> implements IThingDataDAO {

	@Override
	public void insert(List<ThingData> entities) throws DataAccessException {
		if (null == entities || entities.size() == 0) return;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Query.TABLE, entities.get(0).getTable());
		map.put("list", entities);
		sqlSessionTemplate.insert(obtainSQLID(INSERT_BATCH), map);
	}
	
}
