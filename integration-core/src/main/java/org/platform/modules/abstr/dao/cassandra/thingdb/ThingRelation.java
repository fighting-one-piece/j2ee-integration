package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;

public class ThingRelation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey(value = "relation_id")
	private Long relationId = null;
	@Column(value = "thing_id_1")
	private Long thingId1 = null;
	@Column(value = "thing_id_2")
	private Long thingId2 = null;
	@Column(value = "name")
	private String name = null;
	@Column(value = "date")
	private Date date = null;
	
	public Long getRelationId() {
		return relationId;
	}
	
	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
	
	public Long getThingId1() {
		return thingId1;
	}
	
	public void setThingId1(Long thingId1) {
		this.thingId1 = thingId1;
	}
	
	public Long getThingId2() {
		return thingId2;
	}
	
	public void setThingId2(Long thingId2) {
		this.thingId2 = thingId2;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
