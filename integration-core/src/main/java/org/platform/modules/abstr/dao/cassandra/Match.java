package org.platform.modules.abstr.dao.cassandra;

public enum Match {

	EQ(0, "="),
	NE(1, "!="),
	GT(2, ">"),	
	GE(3, ">="),
	LT(4, "<"),
	LE(5, "<="),
	IN(6, "in");
	
	private int id;
	
	private String symbol = null;
	
	private Match(int id, String symbol) {
		this.id = id;
		this.symbol = symbol;
	}
	
	public int getId() {
		return id;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
}
