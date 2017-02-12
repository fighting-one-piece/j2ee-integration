package org.platform.modules.abstr.mapper;

import java.io.Serializable;

import org.platform.utils.exception.DataAccessException;

public interface GenericMapper<Entity extends Serializable, PK extends Serializable> {

	public Entity readDataByPK(PK pk) throws DataAccessException;
	
	
	
}
