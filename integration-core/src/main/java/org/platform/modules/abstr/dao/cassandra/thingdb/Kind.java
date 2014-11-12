package org.platform.modules.abstr.dao.cassandra.thingdb;

public enum Kind {

	STRING("string"),
	INTEGER("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	DATE("date");
	
	private String name = null;
	
	private Kind(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
