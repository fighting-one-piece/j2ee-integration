package org.platform.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 查询条件*/
public class Query implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 排序-升序*/
	public static final int ORDER_ASC = 1;
	/** 排序-降序*/
	public static final int ORDER_DESC = 2;
	/** 当前页数*/
	public static final String CURRENT_PAGE_NUM = "current_page_num";
	/** 每页行数*/
	public static final String ROW_NUM_PER_PAGE = "row_num_per_page";
	/** 总行数*/
	public static final String TOTAL_ROW_NUM = "total_row_num";
	/** 是否分页*/
	public static final String IS_PAGINATION = "isPagination";
	/** 起始位置*/
	public static final String OFFSET = "offset";
	/** 限制数量*/
	public static final String LIMIT = "limit";
	/** 表名*/
	public static final String TABLE = "table";
	/** Thing表名*/
	public static final String T_TABLE = "thingTable";
	/** Data表名*/
	public static final String D_TABLE = "dataTable";
	/** 关键词*/
	public static final String[] KEYS = new String[]{IS_PAGINATION, OFFSET, LIMIT,
		TABLE, CURRENT_PAGE_NUM, ROW_NUM_PER_PAGE, TOTAL_ROW_NUM};
	
	/** 是否分页*/
	private boolean isPagination = false;
	/** 当前页数*/
	private int currentPageNum = 0;
	/** 每页行数*/
	private int rowNumPerPage = Integer.MAX_VALUE;
	/** 排序属性*/
	private String orderProperty = null;
	/** 排序类型*/
	private int orderType = ORDER_ASC;
	/** Hibernate查询条件Map*/
	private Map<String, QueryItem> hibernateCondition = new HashMap<String, QueryItem>();
	/** MyBatis查询条件Map*/
	private Map<String, Object> mybatisCondition = new HashMap<String, Object>();
	/** 基本属性查询条件Map*/
	private Map<String, Object> basicAttributes = new HashMap<String, Object>();
	/** 数据属性查询条件Map*/
	private Map<String, Object> dataAttributes = new HashMap<String, Object>();
	
	public Query() {
		mybatisCondition.put(IS_PAGINATION, isPagination);
		mybatisCondition.put(OFFSET, 0);
		mybatisCondition.put(LIMIT, rowNumPerPage);
	}

	public void addHibernateCondition(String conditionKey, Object conditionValue) {
		hibernateCondition.put(conditionKey, new QueryItem(conditionKey, conditionValue));
	}

	public void addHibernateCondition(String conditionKey, Object conditionValue, int matchType) {
		hibernateCondition.put(conditionKey, new QueryItem(conditionKey, conditionValue, matchType));
	}

	public void addHibernateCondition(QueryItem queryItem) {
		hibernateCondition.put(queryItem.getConditionKey(), queryItem);
	}
	
	public void addMybatisCondition(String conditionKey, Object conditionValue) {
		mybatisCondition.put(conditionKey, conditionValue);
	}
	
	public void addCondition(String conditionKey, Object conditionValue) {
		mybatisCondition.put(conditionKey, conditionValue);
		hibernateCondition.put(conditionKey, new QueryItem(conditionKey, conditionValue));
	}

	public void addOrderCondition(String orderProperty, int orderType) {
		this.orderProperty = orderProperty;
		this.orderType = orderType;
	}

	public Object obtainConditionValue(String conditionKey) {
		for (Map.Entry<String, QueryItem> entry : hibernateCondition.entrySet()) {
			if (conditionKey.equals(entry.getKey())) {
				return entry.getValue().getConditionValue();
			}
		}
		for (Map.Entry<String, Object> entry : mybatisCondition.entrySet()) {
			if (conditionKey.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public String getOrderCondition() {
		return orderProperty;
	}
	
	public String getDataOrderAttribute() {
		return null;
	}

	public int getOrderType() {
		return orderType;
	}
	
	public boolean isPagination() {
		return isPagination;
	}

	public void setPagination(boolean isPagination) {
		this.isPagination = isPagination;
		mybatisCondition.put(IS_PAGINATION, isPagination);
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

	public int getRowNumPerPage() {
		return rowNumPerPage;
	}

	public void setRowNumPerPage(int rowNumPerPage) {
		this.rowNumPerPage = rowNumPerPage;
		mybatisCondition.put(LIMIT, rowNumPerPage);
	}

	public Map<String, QueryItem> getHibernateCondition() {
		if (null == hibernateCondition) {
			hibernateCondition = new HashMap<String, QueryItem>();
		}
		return hibernateCondition;
	}

	public void setHibernateCondition(Map<String, QueryItem> hibernateCondition) {
		this.hibernateCondition = hibernateCondition;
	}
	
	public Map<String, Object> getMybatisCondition() {
		if (null == mybatisCondition) {
			mybatisCondition = new HashMap<String, Object>();
		}
		return mybatisCondition;
	}

	public void setMybatisCondition(Map<String, Object> mybatisCondition) {
		this.mybatisCondition = mybatisCondition;
	}
	
	public Map<String, Object> getBasicAttributes() {
		return basicAttributes;
	}

	public void setBasicAttributes(Map<String, Object> basicAttributes) {
		this.basicAttributes = basicAttributes;
	}

	public Map<String, Object> getDataAttributes() {
		return dataAttributes;
	}

	public void setDataAttributes(Map<String, Object> dataAttributes) {
		this.dataAttributes = dataAttributes;
	}
	
	

}
