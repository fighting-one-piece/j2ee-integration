package org.platform.modules.lucene.original;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.platform.entity.PKEntity;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.utils.resource.ResourceUtils;


/** Lucene处理类  **/
public class LuceneHandler {
	
	public static Logger logger = Logger.getLogger(LuceneHandler.class);
	/** Lucene版本*/
	public static final Version LUCENE_VERSION = Version.LUCENE_45;
	
	/** 内存索引目录*/
	public static final int DIRECTORY_RAM = 1;
	/** 文件索引目录*/
	public static final int DIRECTORY_FS = 2;

	private ILuceneManager luceneManager = null;
	
	private Analyzer analyzer = null;
	private IndexWriter fsIndexWriter = null;
	private IndexWriter ramIndexWriter = null;
	private ReferenceManager<IndexSearcher> fsReferenceManager = null;
	private ReferenceManager<IndexSearcher> ramReferenceManager = null;
	
	private LuceneHandler() {
		luceneManager = new LuceneManagerImpl();
		initLucene();
	}
	
	private static class LuceneHandlerHolder {
		private static final LuceneHandler instance = new LuceneHandler();
	}
	
	public static LuceneHandler getInstance() {
		return LuceneHandlerHolder.instance;
	}
	
	public IndexSearcher obtainIndexSearcher(int directory) {
		IndexSearcher indexSearcher = null;
		try {
			if (directory == DIRECTORY_RAM) {
				indexSearcher = ramReferenceManager.acquire();
			} else if (directory == DIRECTORY_FS) {
				indexSearcher = fsReferenceManager.acquire();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
		return indexSearcher;
	}
	
	public void insertFSIndex(Object object) {
		try {
			fsIndexWriter.addDocument(object2Document(object));
			fsIndexWriter.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}

	public void insertRAMIndex(Object object) {
		try {
			ramIndexWriter.addDocument(object2Document(object));
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}
	
	public void insertIndexs(List<?> list) {
		try {
			for (Object object : list) {
				ramIndexWriter.addDocument(object2Document(object));
			}
			commitIndex(DIRECTORY_RAM);
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}
	
	public void updateIndex(Term term, Object object) {
		try {
			ramIndexWriter.updateDocument(term, object2Document(object));
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}

	public void deleteIndex(Term term) {
		try {
			ramIndexWriter.deleteDocuments(term);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}

	public void deleteAllIndex() {
		try {
			ramIndexWriter.deleteAll();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	public void mergeIndex(Directory... directories) {
		try {
			fsIndexWriter.addIndexes(directories);
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}
	
	public QueryResult<?> readDataByCondition(QueryCondition condition) {
		Class<?> clazz = (Class<?>) condition.obtainConditionValue(QueryCondition.LUCENE_CLASS);
		String keyword = (String) condition.obtainConditionValue(QueryCondition.LUCENE_KEYWORD);
		Query query = (Query) condition.obtainConditionValue(QueryCondition.LUCENE_QUERY);
		if (null == query) {
			query = obtainQuery(clazz, keyword);
		}
		Filter filter = (Filter) condition.obtainConditionValue(QueryCondition.LUCENE_FILTER);
		Sort sort = (Sort) condition.obtainConditionValue(QueryCondition.LUCENE_SORT);
		int currentPageNum = null != condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) ?
				(Integer) condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) : 0;
		int rowNumPerPage = null != condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) ?
				(Integer) condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) : Integer.MAX_VALUE;
		int topN = currentPageNum * rowNumPerPage < rowNumPerPage ? rowNumPerPage : currentPageNum * rowNumPerPage;
		List<Object> objectList = new ArrayList<>();
		int directory = DIRECTORY_RAM;
		IndexSearcher indexSearcher = obtainIndexSearcher(directory);
		try {
			TopDocs topDocs = readData(indexSearcher, query, filter, topN, sort);
			if (null == topDocs || topDocs.totalHits == 0 ) {           
				releaseIndexSearcher(indexSearcher, directory);
				directory = DIRECTORY_FS;
				indexSearcher = obtainIndexSearcher(directory);
				topDocs = readData(indexSearcher, query, filter, topN, sort);
			}
			topN = null == topDocs ? 0 : topDocs.totalHits;
			if (currentPageNum > 1) {
				int prePageIndexNum = (currentPageNum - 1) * rowNumPerPage - 1;
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				ScoreDoc prePageLastScoreDoc = scoreDocs[prePageIndexNum];
				topDocs = indexSearcher.searchAfter(prePageLastScoreDoc, query, filter,
						rowNumPerPage < topDocs.totalHits ? rowNumPerPage : topDocs.totalHits);
			}
			if (null != topDocs && null != topDocs.scoreDocs) {
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					logger.debug("document: " + document);
					objectList.add(document2Object(document, clazz));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		} finally {
			releaseIndexSearcher(indexSearcher, directory);
		}
		return new QueryResult<>(topN, objectList);
	}
	
	public QueryResult<?> readRAMDataByCondition(QueryCondition condition) {
		Class<?> clazz = (Class<?>) condition.obtainConditionValue(QueryCondition.LUCENE_CLASS);
		String keyword = (String) condition.obtainConditionValue(QueryCondition.LUCENE_KEYWORD);
		Query query = (Query) condition.obtainConditionValue(QueryCondition.LUCENE_QUERY);
		if (null == query) {
			query = obtainQuery(clazz, keyword);
		}
		Filter filter = (Filter) condition.obtainConditionValue(QueryCondition.LUCENE_FILTER);
		Sort sort = (Sort) condition.obtainConditionValue(QueryCondition.LUCENE_SORT);
		int currentPageNum = null != condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) ?
				(Integer) condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) : 0;
		int rowNumPerPage = null != condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) ?
				(Integer) condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) : Integer.MAX_VALUE;
		int topN = currentPageNum * rowNumPerPage < rowNumPerPage ? rowNumPerPage : currentPageNum * rowNumPerPage;
		List<Object> objectList = new ArrayList<>();
		IndexSearcher indexSearcher = obtainIndexSearcher(DIRECTORY_RAM);
		try {
			TopDocs topDocs = readData(indexSearcher, query, filter, topN, sort);
			topN = null == topDocs ? 0 :topDocs.totalHits;
			if (currentPageNum > 1) {
				int prePageIndexNum = (currentPageNum - 1) * rowNumPerPage - 1;
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				ScoreDoc prePageLastScoreDoc = scoreDocs[prePageIndexNum];
				topDocs = indexSearcher.searchAfter(prePageLastScoreDoc, query, filter,
						rowNumPerPage < topDocs.totalHits ? rowNumPerPage : topDocs.totalHits);
			}
			if (null != topDocs && null != topDocs.scoreDocs) {
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					logger.debug("document: " + document);
					objectList.add(document2Object(document, clazz));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		} finally {
			releaseIndexSearcher(indexSearcher, DIRECTORY_RAM);
		}
		return new QueryResult<>(topN, objectList);
	}
	
	public QueryResult<?> readFSDataByCondition(QueryCondition condition) {
		Class<?> clazz = (Class<?>) condition.obtainConditionValue(QueryCondition.LUCENE_CLASS);
		String keyword = (String) condition.obtainConditionValue(QueryCondition.LUCENE_KEYWORD);
		Query query = (Query) condition.obtainConditionValue(QueryCondition.LUCENE_QUERY);
		if (null == query) {
			query = obtainQuery(clazz, keyword);
		}
		Filter filter = (Filter) condition.obtainConditionValue(QueryCondition.LUCENE_FILTER);
		Sort sort = (Sort) condition.obtainConditionValue(QueryCondition.LUCENE_SORT);
		int currentPageNum = null != condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) ?
				(Integer) condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) : 0;
		int rowNumPerPage = null != condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) ?
				(Integer) condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) : Integer.MAX_VALUE;
		int topN = currentPageNum * rowNumPerPage < rowNumPerPage ? rowNumPerPage : currentPageNum * rowNumPerPage;
		List<Object> objectList = new ArrayList<>();
		IndexSearcher indexSearcher = obtainIndexSearcher(DIRECTORY_FS);
		try {
			TopDocs topDocs = readData(indexSearcher, query, filter, topN, sort);
			topN = null == topDocs ? 0 : topDocs.totalHits;
			if (currentPageNum > 1) {
				int prePageIndexNum = (currentPageNum - 1) * rowNumPerPage - 1;
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				ScoreDoc prePageLastScoreDoc = scoreDocs[prePageIndexNum];
				topDocs = indexSearcher.searchAfter(prePageLastScoreDoc, query, filter,
						rowNumPerPage < topDocs.totalHits ? rowNumPerPage : topDocs.totalHits);
			}
			if (null != topDocs && null != topDocs.scoreDocs) {
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					logger.debug("document: " + document);
					objectList.add(document2Object(document, clazz));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		} finally {
			releaseIndexSearcher(indexSearcher, DIRECTORY_FS);
		}
		return new QueryResult<>(topN, objectList);
	}
	
	private TopDocs readData(IndexSearcher indexSearcher, Query query, Filter filter, int topN, Sort sort) throws IOException {
		TopDocs topDocs = null;
		if (null == filter && null == sort) {
			topDocs = indexSearcher.search(query, topN);
		} else if (null != filter && null == sort) {
			topDocs = indexSearcher.search(query, filter, topN);
		} else if (null == filter && null != sort) {
			topDocs = indexSearcher.search(query, topN, sort);
		} else if (null != filter && null != sort) {
			topDocs = indexSearcher.search(query, filter, topN, sort);
		}
		return topDocs;
	}

	public void commitIndex(int directory) {
		try {
			if (directory == DIRECTORY_RAM) {
				ramIndexWriter.commit();
			} else if (directory == DIRECTORY_FS) {
				fsIndexWriter.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}
	
	public void releaseIndexSearcher(IndexSearcher indexSearcher, int directory) {
		try {
			if (directory == DIRECTORY_RAM) {
				ramReferenceManager.release(indexSearcher);
			} else if (directory == DIRECTORY_FS) {
				fsReferenceManager.release(indexSearcher);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}

	public Document object2Document(Object object) {
		Document document = new Document();
		for (java.lang.reflect.Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
				Object fieldValue = field.get(object);
				if (Collection.class.isAssignableFrom(fieldType) || PKEntity.class.isAssignableFrom(fieldType) || 
						"serialVersionUID".equals(field.getName()) || null == fieldValue) {
					continue;
				} 
				if (String.class.isAssignableFrom(fieldType)) {
					document.add(new StringField(fieldName, String.valueOf(fieldValue), Store.YES));
				} else if (Integer.class.isAssignableFrom(fieldType)) {
					document.add(new IntField(fieldName, Integer.parseInt(String.valueOf(fieldValue)), Store.YES));
				} else if (Long.class.isAssignableFrom(fieldType)) {
					document.add(new LongField(fieldName, Long.parseLong(String.valueOf(fieldValue)), Store.YES));
				} else if (Double.class.isAssignableFrom(fieldType)) {
					document.add(new DoubleField(fieldName, Double.parseDouble(String.valueOf(fieldValue)), Store.YES));
				} else if (Float.class.isAssignableFrom(fieldType)) {
					document.add(new FloatField(fieldName, Float.parseFloat(String.valueOf(fieldValue)), Store.YES));
				} else if (Date.class.isAssignableFrom(fieldType)) {
					document.add(new LongField(fieldName, ((Date) fieldValue).getTime(), Store.YES));
				} 
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				logger.debug(e.getMessage(), e);
			}
		}
		return document;
	}

	public Object document2Object(Document document, Class<?> clazz) {
		Object object = null;
		try {
			object = clazz.newInstance();
			for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				Class<?> fieldType = field.getType();
				IndexableField indexableField = document.getField(field.getName());
				if (Collection.class.isAssignableFrom(fieldType) || PKEntity.class.isAssignableFrom(fieldType)
						|| null == indexableField) {
					continue;
				}
				if (String.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.stringValue());
				} else if (Integer.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.numericValue().intValue());
				} else if (Long.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.numericValue().longValue());
				} else if (Double.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.numericValue().doubleValue());
				} else if (Float.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.numericValue().floatValue());
				} else if (Date.class.isAssignableFrom(fieldType)) {
					field.set(object, new Date(indexableField.numericValue().longValue()));
				} 
			}
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
		return object;
	}

	public String highLighter(Analyzer analyzer, Query query, String fieldName, String fieldValue) throws Exception {
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		Formatter fmt = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter highLighter = new Highlighter(fmt, scorer);
		highLighter.setTextFragmenter(fragmenter);
		String highLighterTxt = highLighter.getBestFragments(
					analyzer.tokenStream(fieldName, new StringReader(fieldValue)), fieldValue, 3, "......\n");
		if(highLighterTxt == null) {
			return fieldValue;
		}
		return highLighterTxt;
	}
	
	@SuppressWarnings("resource")
	private void initLucene() {
		try {
//			analyzer = obtainAnalyzer();
//			IndexWriterConfig fsIndexWriterConfig = new IndexWriterConfig(LUCENE_VERSION, analyzer);
//			fsIndexWriter = new IndexWriter(obtainDirectory(DIRECTORY_FS), fsIndexWriterConfig);
			fsIndexWriter = luceneManager.getIndexWriter();
			TrackingIndexWriter fsTrackingIndexWriter = new TrackingIndexWriter(fsIndexWriter);
			SearcherFactory fsSearcherFactory = new SearcherFactory();
			fsReferenceManager = new SearcherManager(fsIndexWriter, true, fsSearcherFactory);
			ControlledRealTimeReopenThread<IndexSearcher> fsControlledRealTimeReopenThread = 
					new ControlledRealTimeReopenThread<IndexSearcher>(fsTrackingIndexWriter, fsReferenceManager, 5.0, 0.025);
			fsControlledRealTimeReopenThread.setName("FSControlledRealTimeReopenThread");
			fsControlledRealTimeReopenThread.setDaemon(true);
			fsControlledRealTimeReopenThread.start();
			
			IndexWriterConfig ramIndexWriterConfig = new IndexWriterConfig(LUCENE_VERSION, analyzer);
			ramIndexWriter = new IndexWriter(obtainDirectory(DIRECTORY_RAM), ramIndexWriterConfig);
			TrackingIndexWriter ramTrackingIndexWriter = new TrackingIndexWriter(ramIndexWriter);
			SearcherFactory ramSearcherFactory = new SearcherFactory();
			ramReferenceManager = new SearcherManager(ramIndexWriter, true, ramSearcherFactory);
			ControlledRealTimeReopenThread<IndexSearcher> ramControlledRealTimeReopenThread = 
					new ControlledRealTimeReopenThread<IndexSearcher>(ramTrackingIndexWriter, ramReferenceManager, 5.0, 0.025);
			ramControlledRealTimeReopenThread.setName("RAMControlledRealTimeReopenThread");
			ramControlledRealTimeReopenThread.setDaemon(true);
			ramControlledRealTimeReopenThread.start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
	}
	
	private Directory obtainDirectory(int directory) {
		try {
			if (directory == DIRECTORY_RAM) {
				return new RAMDirectory();
			} else {
				System.out.println(ResourceUtils.getAbsolutePath("index"));
				return FSDirectory.open(new File(ResourceUtils.getAbsolutePath("index")));
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
		return null;
	}
	
	private Query obtainQuery(Class<?> clazz, String keyword) {
		List<String> fields = new ArrayList<String>();
		for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			Class<?> type = field.getType();
			if (Collection.class.isAssignableFrom(type) || PKEntity.class.isAssignableFrom(type)) {
				continue;
			} 
			fields.add(field.getName());
		}
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(LUCENE_VERSION, fields.toArray(new String[0]), analyzer);
		Query query = null;
		try {
			query = queryParser.parse(keyword);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.debug(e.getMessage(), e);
		}
		return query;
	}
}
