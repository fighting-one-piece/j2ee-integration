package org.platform.modules.abstr.dao.cassandra;

/** 查询条目*/
public class QueryItem {
	
	/** 条件*/
	private String conditionKey = null;
	/** 条件值*/
	private Object conditionValue = null;
	/** 匹配类型*/
	private Match match = Match.EQ;

	public QueryItem(String conditionKey, Object conditionValue) {
		super();
		this.conditionKey = conditionKey;
		this.conditionValue = conditionValue;
		this.match = Match.EQ;
	}

	public QueryItem(String conditionKey, Object conditionValue, Match match) {
		super();
		this.conditionKey = conditionKey;
		this.conditionValue = conditionValue;
		this.match = match;
	}

	public String getConditionKey() {
		return conditionKey;
	}

	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}

	public Object getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(Object conditionValue) {
		this.conditionValue = conditionValue;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

}
