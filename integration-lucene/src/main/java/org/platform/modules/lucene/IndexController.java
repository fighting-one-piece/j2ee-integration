package org.platform.modules.lucene;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;

/** 索引控制*/
public class IndexController {
	
	private Map<Integer, IIndex> indexes = null;
	
	private Map<String, Analyzer> analyzeries = null;

	private IndexController(){
		System.out.println("initializing indexes");
		
		indexes = new HashMap<Integer, IIndex>();
		indexes.put(IIndex.RAM, new RAMIndex());
		indexes.put(IIndex.FILE, new FSIndex());
		indexes.put(IIndex.FILE_NRT, new FSIndex(true));
		
		analyzeries = new HashMap<String, Analyzer>();
		analyzeries.put(IIndex.ANALYZER_STANDARD, new StandardAnalyzer(IIndex.VERSION));
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_SIMPLE, new SimpleAnalyzer());
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_COMPLEX, new ComplexAnalyzer());
		analyzeries.put(IIndex.ANALYZER_MMSEG4J_MAXWORD, new MMSegAnalyzer());
	}
	
	private static class IndexControllerHolder {
		private static IndexController instance = new IndexController();
	}
	
	public static IndexController getInstance() {
		return IndexControllerHolder.instance;
	}
	
	public IIndex obtainIndex(int indexType) {
		if (null == indexes.get(indexType)) {
			System.out.println("未找到相关索引");
		}
		return indexes.get(indexType);
	}
	
	public boolean existIndex(int indexType) {
		return null == indexes.get(indexType) ? false : true;
	}
	
	public void addIndex(int indexType, IIndex index) {
		indexes.put(indexType, index);
	}
	
	public void removeIndex(int indexType) {
		indexes.remove(indexType);
	}
	
	public IndexWriter obtainIndexWriter(int indexType) {
		return existIndex(indexType) ? obtainIndex(indexType).obtainIndexWriter() : null;
	}
	
	public IndexSearcher obtainIndexSearcher(int indexType) {
		return existIndex(indexType) ? obtainIndex(indexType).obtainIndexSearcher() : null;
	}
	
	public void releaseIndexSearcher(int indexType, IndexSearcher indexSearcher) {
		if (existIndex(indexType)) {
			obtainIndex(indexType).releaseIndexSearcher(indexSearcher);
		} else {
			System.out.println("索引不存在");
		}
	}
	
	public void closeIndexWriter(int indexType) {
		if (existIndex(indexType)) {
			obtainIndex(indexType).closeIndexWriter();
			removeIndex(indexType);
		} else {
			System.out.println("索引不存在");
		}
	}
	
	public Analyzer obtainAnalyzer(String analyzerType) {
		if (null == analyzeries.get(analyzerType)) {
			System.out.println("未找到相关分词器");
		}
		return analyzeries.get(analyzerType);
	}
}
