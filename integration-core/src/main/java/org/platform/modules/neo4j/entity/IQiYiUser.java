package org.platform.modules.neo4j.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class IQiYiUser {

	@GraphId
	private Long id = null;
	
}

