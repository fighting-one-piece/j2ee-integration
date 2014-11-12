package org.platform.modules.abstr.dao.cassandra.thingdb;

public enum Sort {

	HOT("hot", Sort.DESC), 
	NEW("new", Sort.DESC), 
	TOP("top", Sort.DESC),
	DATE("date", Sort.DESC),
	SCORE("score", Sort.DESC),
	CONFIDENCE("confidence", Sort.DESC),
	CONTROVERSIAL("controversial", Sort.DESC);
	
	public static final int ASC = 1;
	
	public static final int DESC = 2;

	private String name = null;

	private int order;
	
	private Sort(String name, int order) {
		this.name = name;
		this.order = order;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOrder() {
		return order;
	}
	
}
