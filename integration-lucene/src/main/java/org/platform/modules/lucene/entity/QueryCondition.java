package org.platform.modules.lucene.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 查询条件*/
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 当前页数*/
	public static final String CURRENT_PAGE_NUM = "current_page_num";
	/** 每页行数*/
	public static final String ROW_NUM_PER_PAGE = "row_num_per_page";
	/** 总行数*/
	public static final String TOTAL_ROW_NUM = "total_row_num";
	/** 是否分页*/
	public static final String IS_PAGINATION = "isPagination";
	
	/** Lucene 对象类*/
	public static final String INDEX = "index";
	/** Lucene 对象类*/
	public static final String ENTITY_CLASS = "entity_class";
	/** Lucene 关键词*/
	public static final String KEYWORD = "keyword";
	/** Lucene 分词器*/
	public static final String ANALYZER = "analyzer";
	/** Lucene Query*/
	public static final String QUERY = "query";
	/** Lucene Filter*/
	public static final String FILTER = "filter";
	/** Lucene Sort*/
	public static final String SORT = "sort";
	/** Lucene HighLighter Fields*/
	public static final String HIGHLIGHTER_FIELDS = "highlighter_fields";
	
	
	/** 当前页数*/
	private int currentPageNum = 0;
	/** 每页行数*/
	private int rowNumPerPage = Integer.MAX_VALUE;
	/** 查询条件Map*/
	private Map<String, Object> conditions = new HashMap<String, Object>();
	
	public QueryCondition() {
	}
	
	public void addCondition(String conditionKey, Object conditionValue) {
		conditions.put(conditionKey, conditionValue);
	}

	public Object getConditionValue(String conditionKey) {
		for (Map.Entry<String, Object> entry : conditions.entrySet()) {
			if (conditionKey.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
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
	}

	public Map<String, Object> getConditions() {
		if (null == conditions) {
			conditions = new HashMap<String, Object>();
		}
		return conditions;
	}

	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}

}
