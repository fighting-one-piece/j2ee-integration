package org.platform.modules.abstr.common;

public enum Kind {

	STRING("string"),
	INTEGER("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	BOOLEAN("boolean"),
	DATE("date");
	
	private String name = null;
	
	private Kind(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
