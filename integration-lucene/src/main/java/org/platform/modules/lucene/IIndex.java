package org.platform.modules.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.Version;

/** 索引接口,获取IndexWriter,IndexSearcher*/
public interface IIndex {
	
	/** Lucene版本*/
	public static final Version VERSION = Version.LUCENE_45;
	
	/** 分词器类型*/
	/** 标准分词器*/
	public static final String ANALYZER_STANDARD = "standard";
	/** MMSEG4J 简单分词器*/
	public static final String ANALYZER_MMSEG4J_SIMPLE = "mmseg4j_simple";
	/** MMSEG4J 复杂分词器*/
	public static final String ANALYZER_MMSEG4J_COMPLEX = "mmseg4j_complex";
	/** MMSEG4J 最多词分词器*/
	public static final String ANALYZER_MMSEG4J_MAXWORD = "mmseg4j_maxword";
	
	/** 内存索引*/
	public static final int RAM = 11;
	/** 内存临时索引*/
	public static final int RAM_TEMP = 12;
	/** 文件索引*/
	public static final int FILE = 21;
	/** 文件索引--准实时*/
	public static final int FILE_NRT = 22;
	
	/**
	 * 获取IndexWriter对象
	 * @return
	 */
	public IndexWriter obtainIndexWriter();
	
	/**
	 * 获取IndexSearcher对象
	 * @return
	 */
	public IndexSearcher obtainIndexSearcher();
	
	/**
	 * 关闭IndexWriter
	 */
	public void closeIndexWriter();
	
	/**
	 * 释放IndexSearcher
	 * @param indexSearcher
	 */
	public void releaseIndexSearcher(IndexSearcher indexSearcher);

}
