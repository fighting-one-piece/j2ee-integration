package org.platform.modules.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;

/** 基于内存、文件混合索引简单的操作*/
public class SimpleIndexManager extends AbstrIndexManager {
	
	@Override
	public void insert(Object... objects) {
		List<Document> documents = new ArrayList<Document>();
		for (Object object : objects) {
			documents.add(object2Document(object));
		}
		try {
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).addDocuments(documents);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	@Override
	public void update(Term term, Object object) {
		try {
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).updateDocument(term, object2Document(object));
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Term term) {
		try {
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).deleteDocuments(term);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).deleteAll();
			IndexController.getInstance().obtainIndexWriter(IIndex.FILE).deleteAll();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void commit() {
		try {
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).commit();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void merge(Directory...directories) {
		try {
			System.out.println("merge index start");
			//合并内存索引到文件索引中，合并完成后提交文件索引，新�?��个内存索�?
			IndexController.getInstance().obtainIndexWriter(IIndex.RAM).commit();
			IndexController.getInstance().obtainIndexWriter(IIndex.FILE).addIndexes(
					IndexController.getInstance().obtainIndexWriter(IIndex.RAM).getDirectory());
			IndexController.getInstance().obtainIndexWriter(IIndex.FILE).commit();
			IndexController.getInstance().closeIndexWriter(IIndex.RAM);
			IndexController.getInstance().addIndex(IIndex.RAM, new RAMIndex());
			System.out.println("merge index end");
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}	
	}
	
	@Override
	public QueryResult<?> readByCondition(QueryCondition condition) {
		Class<?> clazz = (Class<?>) condition.obtainConditionValue(QueryCondition.LUCENE_CLASS);
		String keyword = (String) condition.obtainConditionValue(QueryCondition.LUCENE_KEYWORD);
		Query query = (Query) condition.obtainConditionValue(QueryCondition.LUCENE_QUERY);
		if (null == query) query = obtainQuery(clazz, keyword);
		Analyzer analyzer = (Analyzer) condition.obtainConditionValue(QueryCondition.LUCENE_ANALYZER);
		Filter filter = (Filter) condition.obtainConditionValue(QueryCondition.LUCENE_FILTER);
		Sort sort = (Sort) condition.obtainConditionValue(QueryCondition.LUCENE_SORT);
		String[] highLighterFields = (String[]) condition.obtainConditionValue(QueryCondition.LUCENE_HIGHLIGHTER_FIELDS);
		int currentPageNum = null != condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) ?
				(Integer) condition.obtainConditionValue(QueryCondition.CURRENT_PAGE_NUM) : 0;
		int rowNumPerPage = null != condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) ?
				(Integer) condition.obtainConditionValue(QueryCondition.ROW_NUM_PER_PAGE) : Integer.MAX_VALUE;
		int topN = currentPageNum * rowNumPerPage < rowNumPerPage ? rowNumPerPage : currentPageNum * rowNumPerPage;
		List<Object> objectList = new ArrayList<>();
		//查询步骤1、从内存索引中查询结果 2、从文件索引中查询结果
		int indexType = IIndex.RAM;
		IndexSearcher indexSearcher = null;
		TopDocs topDocs = null;
		try {
			indexSearcher = IndexController.getInstance().obtainIndexSearcher(indexType);
			if (null != indexSearcher) {
				topDocs = readData(indexSearcher, query, filter, topN, sort);
				topN = (null == topDocs || topDocs.totalHits == 0) ? topN : topDocs.totalHits;
			}
			if (null == topDocs || topDocs.totalHits == 0) {
				IndexController.getInstance().releaseIndexSearcher(indexType, indexSearcher);
				indexType = IIndex.FILE;
				indexSearcher = IndexController.getInstance().obtainIndexSearcher(indexType);
				if (null != indexSearcher) {
					topDocs = readData(indexSearcher, query, filter, topN, sort);
					topN = null == topDocs ? 0 : topDocs.totalHits;
				}
			}
			System.out.println("query result from " + indexType);
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
					Object object = document2Object(document, clazz);
					if (null == analyzer) {
						highLighterFast(indexSearcher, query, scoreDoc.doc, object, highLighterFields);
					} else {
						highLighter(analyzer, query, document, object, highLighterFields);
					}
					objectList.add(object);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		} finally {
			IndexController.getInstance().releaseIndexSearcher(indexType, indexSearcher);
		}
		return new QueryResult<>(topN, objectList);
	}
	
}
