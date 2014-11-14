package org.platform.modules.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;

/** 索引管理接口*/
public interface IIndexManager {
	
	/**
	 * 新增索引
	 * @param objects 索引对象
	 */
	public void insert(Object... objects);
	
	/**
	 * 更新索引对象
	 * @param term 已索引的条目 
	 * @param object 待更新的对象
	 */
	public void update(Term term, Object object);
	
	/**
	 * 删除索引条目
	 * @param term
	 */
	public void delete(Term term);
	
	/**
	 * 删除�?��索引
	 */
	public void deleteAll();
	
	/**
	 * 提交索引
	 */
	public void commit();
	
	/**
	 * 合并索引
	 * @param directories 索引目录
	 */
	public void merge(Directory...directories);
	
	/**
	 * 根据条件查询索引
	 * @param condition 条件
	 * @return
	 */
	public QueryResult<?> readByCondition(Query condition);
	
}
