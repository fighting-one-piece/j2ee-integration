package org.platform.modules.abstr.dao.cassandra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.platform.modules.abstr.common.ThingUtils;

/** 查询条件*/
public class Query implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 排序-升序*/
	public static final String ORDER_ASC = "asc";
	/** 排序-降序*/
	public static final String ORDER_DESC = "desc";
	/** 起始位置*/
	public static final String OFFSET = "offset";
	/** 限制数量*/
	public static final String LIMIT = "limit";
	/** 是否分页*/
	private boolean isPagination = false;
	/** 当前页数*/
	private int currentPageNum = 0;
	/** 限制行数*/
	private int limit = Integer.MAX_VALUE;
	/** 排序属性*/
	private String orderAttribute = null;
	/** 排序类型*/
	private String orderType = ORDER_ASC;
	/** 查询条件列表*/
	private List<QueryItem> conditions = new ArrayList<QueryItem>();
	/** 基本属性*/
	private List<QueryItem> basicAttributes = new ArrayList<QueryItem>();
	/** 数据属性*/
	private List<QueryItem> dataAttributes = new ArrayList<QueryItem>();
	
	public Query() {
	}

	public void addCondition(String conditionKey, Object conditionValue) {
		addCondition(new QueryItem(conditionKey, conditionValue));
	}

	public void addCondition(String conditionKey, Object conditionValue, Match match) {
		addCondition(new QueryItem(conditionKey, conditionValue, match));
	}

	public void addCondition(QueryItem queryItem) {
		String conditionKey = queryItem.getConditionKey();
		checkConditionKey(conditionKey);
		conditions.add(queryItem);
		if (ThingUtils.isBasicAttribute(conditionKey)) {
			handleNewAttribute(getBasicAttributes(), queryItem);
		} else {
			handleNewAttribute(getDataAttributes(), queryItem);
		}
	}
	
	public void addOrderCondition(String orderAttribute, String orderType) {
		this.orderAttribute = orderAttribute;
		this.orderType = orderType;
	}

	public Object obtainConditionValue(String conditionKey) {
		for (QueryItem condition : conditions) {
			if (conditionKey.equalsIgnoreCase(condition.getConditionKey())) {
				return condition.getConditionValue();
			}
		}
		return null;
	}
	
	public String getOrderAttribute() {
		return orderAttribute;
	}

	public String getOrderType() {
		return orderType;
	}
	
	public boolean isPagination() {
		return isPagination;
	}

	public void setPagination(boolean isPagination) {
		this.isPagination = isPagination;
	}

	public int getCurrentPageNum() {
		if (currentPageNum <= 1) {
			currentPageNum = 1;
		}
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<QueryItem> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryItem> conditions) {
		if (null == this.conditions) {
			this.conditions = new ArrayList<QueryItem>();
		}
		this.conditions = conditions;
	}
	
	public List<QueryItem> getBasicAttributes() {
		if (null == basicAttributes) {
			basicAttributes = new ArrayList<QueryItem>();
		}
		return basicAttributes;
	}
	
	public int getBasicAttributesLength() {
		return getBasicAttributes().size();
	}

	public void setBasicAttributes(List<QueryItem> basicAttributes) {
		this.basicAttributes = basicAttributes;
	}

	public List<QueryItem> getDataAttributes() {
		if (null == dataAttributes) {
			dataAttributes = new ArrayList<QueryItem>();
		}
		return dataAttributes;
	}
	
	public int getDataAttributesLength() {
		return getDataAttributes().size();
	}

	public void setDataAttributes(List<QueryItem> dataAttributes) {
		this.dataAttributes = dataAttributes;
	}

	public void checkConditionKey(String conditionKey) {
		
	}
	
	private void handleNewAttribute(List<QueryItem> items, QueryItem item) {
		String conditionKey = item.getConditionKey();
		Iterator<QueryItem> iterator = items.iterator();
		while (iterator.hasNext()) {
			QueryItem temp = iterator.next();
			if (temp.getConditionKey().equalsIgnoreCase(conditionKey)) {
				iterator.remove();
			}
		}
		items.add(item);
	}

}
