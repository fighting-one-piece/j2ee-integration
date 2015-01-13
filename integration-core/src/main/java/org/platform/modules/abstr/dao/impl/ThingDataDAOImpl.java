package org.platform.modules.abstr.dao.impl;

import org.platform.modules.abstr.dao.IThingDataDAO;
import org.platform.modules.abstr.entity.ThingData;
import org.springframework.stereotype.Repository;

@Repository("thingDataDAO")
public class ThingDataDAOImpl extends GenericMyBatisDAOImpl<ThingData, Long> implements IThingDataDAO {

}
