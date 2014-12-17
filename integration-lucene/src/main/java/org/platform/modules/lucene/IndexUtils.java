package org.platform.modules.lucene;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;
import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

/** 索引工具*/
public class IndexUtils {
	
	private static Map<Integer, IIndex> indexes = null;
	
	private static Map<String, Analyzer> analyzeries = null;
	
	static {
		indexes = new HashMap<Integer, IIndex>();
		indexes.put(IIndex.RAM, new RAMIndex());
		indexes.put(IIndex.FILE, new FSIndex());
		indexes.put(IIndex.FILE_NRT, new FSIndex(true));
		
		analyzeries = new HashMap<String, Analyzer>();
		analyzeries.put(IIndex.ANALYZER_STANDARD, new StandardAnalyzer(IIndex.VERSION));
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_SIMPLE, new MMSegAnalyzer());
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_SIMPLE, new SimpleAnalyzer());
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_COMPLEX, new ComplexAnalyzer());
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_MAXWORD, new MaxWordAnalyzer());
	}

	public static IIndex obtainIndex(int indexType) {
		if (null == indexes.get(indexType)) {
			System.out.println("未找到相关索引");
		}
		return indexes.get(indexType);
	}
	
	public static boolean existIndex(int indexType) {
		return null == indexes.get(indexType) ? false : true;
	}
	
	public static void addIndex(int indexType, IIndex index) {
		indexes.put(indexType, index);
	}
	
	public static void removeIndex(int indexType) {
		indexes.remove(indexType);
	}
	
	public static IndexWriter obtainIndexWriter(int indexType) {
		return existIndex(indexType) ? obtainIndex(indexType).obtainIndexWriter() : null;
	}
	
	public static IndexSearcher obtainIndexSearcher(int indexType) {
		return existIndex(indexType) ? obtainIndex(indexType).obtainIndexSearcher() : null;
	}
	
	public static void releaseIndexSearcher(int indexType, IndexSearcher indexSearcher) {
		if (existIndex(indexType)) {
			obtainIndex(indexType).releaseIndexSearcher(indexSearcher);
		} else {
			System.out.println("索引不存在");
		}
	}
	
	public static void closeIndexWriter(int indexType) {
		if (existIndex(indexType)) {
			obtainIndex(indexType).closeIndexWriter();
			removeIndex(indexType);
		} else {
			System.out.println("索引不存在");
		}
	}
	
	public static Analyzer obtainDefaultAnalyzer() {
		return analyzeries.get(IIndex.ANALYZER_MMSEG4J_MAXWORD);
	}
	
	public static Analyzer obtainAnalyzer(String analyzerType) {
		if (null == analyzeries.get(analyzerType)) {
			System.out.println("未找到相关分词器");
		}
		return analyzeries.get(analyzerType);
	}
}
