package org.platform.entity;

/** 查询条目*/
public class QueryItem {
	/** 等于*/
	public static final int MATCH_EQ = 0;
	/** 不等于*/
	public static final int MATCH_NE = 1;
	/** 大于*/
	public static final int MATCH_GT = 2;
	/** 大于等于*/
	public static final int MATCH_GE = 3;
	/** 小于*/
	public static final int MATCH_LT = 4;
	/** 小于等于*/
	public static final int MATCH_LE = 5;
	/** 范围之内*/
	public static final int MATCH_BETWEEN = 6;
	/** IN范围*/
	public static final int MATCH_IN = 7;
	/** 或者*/
	public static final int MATCH_OR = 8;
	/** 与*/
	public static final int MATCH_AND = 9;
	/** 为空*/
	public static final int MATCH_ISNULL = 10;
	/** 不为空*/
	public static final int MATCH_ISNOTNULL = 11;
	/** 模糊查询*/
	public static final int MATCH_LIKE = 12;
	/** 模糊查询*/
	public static final int MATCH_LIKE_START = 13;
	/** 模糊查询*/
	public static final int MATCH_LIKE_END = 14;

	/** 条件*/
	private String conditionKey = null;
	/** 条件值*/
	private Object conditionValue = null;
	/** 匹配类型*/
	private int matchType = MATCH_EQ;

	public QueryItem(String conditionKey, Object conditionValue) {
		super();
		this.conditionKey = conditionKey;
		this.conditionValue = conditionValue;
		this.matchType = MATCH_EQ;
	}

	public QueryItem(String conditionKey, Object conditionValue, int matchType) {
		super();
		this.conditionKey = conditionKey;
		this.conditionValue = conditionValue;
		this.matchType = matchType;
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

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}

}
