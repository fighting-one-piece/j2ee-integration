package org.platform.modules.neo4j.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "FOLLOW")
public class IQiYiUserRelation {

	@StartNode
	private IQiYiUser start = null;
	@EndNode
	private IQiYiUser end = null;
	
}

