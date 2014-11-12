package org.platform.modules.abstr.dao.cassandra;

import java.util.ArrayList;
import java.util.List;

public class QueryResult<Entity> {
	/** 数据集合*/
	private List<Entity> resultList = null;
	/** 总行数*/
	private int totalRowNum = 0;

	public QueryResult() {

	}

	public QueryResult(int totalRowNum, List<Entity> resultList) {
		this.totalRowNum = totalRowNum;
		this.resultList = resultList;
	}

	public QueryResult(int currentPageNum, int rowNumPerPage, List<Entity> resultList) {
		this.totalRowNum = resultList.size();
		this.resultList = resultList;

		int firstRowNum = getFirstRowNum(currentPageNum, rowNumPerPage);
		int lastRowNum = firstRowNum + rowNumPerPage;

		List<Entity> list = new ArrayList<Entity>();
		for (int i = firstRowNum, rowNum = resultList.size(); i < rowNum && i < lastRowNum; i++) {
			list.add(resultList.get(i));
		}
		this.resultList.clear();
		this.resultList = list;
	}

	public List<Entity> getResultList() {
		if (resultList == null) {
			resultList = new ArrayList<Entity>();
		}
		return resultList;
	}

	public void setResultList(List<Entity> resultList) {
		this.resultList = resultList;
	}

	public int getTotalRowNum() {
		return totalRowNum;
	}

	public void setTotalRowNum(int totalRowNum) {
		this.totalRowNum = totalRowNum;
	}

	public int getTotalPageNum(int rowNumPerPage) {
		if (rowNumPerPage == 0) {
			return 0;
		}
		return (resultList.size() - 1) / rowNumPerPage + 1;
	}

	public int getFirstRowNum(int currentPageNum, int rowNumPerPage) {
		if (currentPageNum <= 1) {
			currentPageNum = 1;
		}
		if (currentPageNum > getTotalPageNum(rowNumPerPage)) {
			currentPageNum = getTotalPageNum(rowNumPerPage);
		}
		return (currentPageNum -1) * rowNumPerPage;
	}

}
