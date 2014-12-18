package org.platform.modules.lucene.entity;

import java.io.Serializable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

/** 查询条件*/
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Lucene 对象类*/
	public static final String INDEX = "index";
	
	/** 实体类*/
	private Class<?> entityClass = null;
	/** 关键词*/
	private String keyword = null;
	/** 分词器*/
	private Analyzer analyzer = null;
	/** 过滤器*/
	private Filter filter = null;
	/** 排序*/
	private Sort sort = null;
	/** Query*/
	private Query query = null;
	/** 查询类型标识*/
	private String queryTag = null;
	/** 查询域*/
	private String[] queryFields = null;
	/** 高亮域*/
	private String[] highLighterFields = null;
	/** 当前页数*/
	private int currentPageNum = 0;
	/** 每页行数*/
	private int rowNumPerPage = Integer.MAX_VALUE;
	
	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public String getQueryTag() {
		return queryTag;
	}

	public void setQueryTag(String queryTag) {
		this.queryTag = queryTag;
	}

	public String[] getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(String[] queryFields) {
		this.queryFields = queryFields;
	}

	public String[] getHighLighterFields() {
		return highLighterFields;
	}

	public void setHighLighterFields(String[] highLighterFields) {
		this.highLighterFields = highLighterFields;
	}

	public int getCurrentPageNum() {
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

	
	
}
