package org.platform.modules.lucene.biz.impl;

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
import org.platform.entity.QueryResult;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IndexUtils;
import org.platform.modules.lucene.RAMIndex;
import org.platform.modules.lucene.entity.QueryCondition;

/** 基于内存、文件混合索引简单的操作*/
public class SimpleIndexBusinessImpl extends AbstrIndexBusinessImpl {
	
	@Override
	public void insert(Object... objects) {
		List<Document> documents = new ArrayList<Document>();
		for (Object object : objects) {
			documents.add(object2Document(object));
		}
		try {
			IndexUtils.obtainIndexWriter(IIndex.RAM).addDocuments(documents);
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}
	}

	@Override
	public void update(Term term, Object object) {
		try {
			IndexUtils.obtainIndexWriter(IIndex.RAM).updateDocument(term, object2Document(object));
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Term term) {
		try {
			IndexUtils.obtainIndexWriter(IIndex.RAM).deleteDocuments(term);
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			IndexUtils.obtainIndexWriter(IIndex.RAM).deleteAll();
			IndexUtils.obtainIndexWriter(IIndex.FILE).deleteAll();
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void commit() {
		try {
			IndexUtils.obtainIndexWriter(IIndex.RAM).commit();
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void merge(Directory... directories) {
		try {
			System.out.println("merge index start");
			//合并内存索引到文件索引中，合并完成后提交文件索引，新增内存索引
			IndexUtils.obtainIndexWriter(IIndex.RAM).commit();
			IndexUtils.obtainIndexWriter(IIndex.FILE).addIndexes(
					IndexUtils.obtainIndexWriter(IIndex.RAM).getDirectory());
			IndexUtils.obtainIndexWriter(IIndex.FILE).commit();
			IndexUtils.closeIndexWriter(IIndex.RAM);
			IndexUtils.addIndex(IIndex.RAM, new RAMIndex());
			System.out.println("merge index end");
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}	
	}
	
	@Override
	public QueryResult<?> readDataListByCondition(QueryCondition condition) {
		Query query = obtainQuery(condition);
		Filter filter = condition.getFilter();
		Sort sort = obtainSort(condition);
		String[] highLighterFields = condition.getHighLighterFields();
		int currentPageNum = condition.getCurrentPageNum();
		int rowNumPerPage = condition.getRowNumPerPage(); 
		int topN = currentPageNum * rowNumPerPage < rowNumPerPage ? rowNumPerPage : currentPageNum * rowNumPerPage;
		List<Object> objectList = new ArrayList<>();
		//查询步骤1、从内存索引中查询结果 2、从文件索引中查询结果
		int indexType = IIndex.RAM;
		IndexSearcher indexSearcher = null;
		TopDocs topDocs = null;
		try {
			indexSearcher = IndexUtils.obtainIndexSearcher(indexType);
			if (null != indexSearcher) {
				topDocs = readDataList(indexSearcher, query, filter, topN, sort);
				topN = (null == topDocs || topDocs.totalHits == 0) ? topN : topDocs.totalHits;
			}
			if (null == topDocs || topDocs.totalHits == 0) {
				IndexUtils.releaseIndexSearcher(indexType, indexSearcher);
				indexType = IIndex.FILE;
				indexSearcher = IndexUtils.obtainIndexSearcher(indexType);
				if (null != indexSearcher) {
					topDocs = readDataList(indexSearcher, query, filter, topN, sort);
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
				Analyzer analyzer = condition.getAnalyzer();
				if (null == analyzer) analyzer = IndexUtils.obtainDefaultAnalyzer();
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					LOG.debug("document: " + document);
					Object object = document2Object(document, condition.getEntityClass());
					highLighter(analyzer, query, document, object, highLighterFields);
					objectList.add(object);
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
		} finally {
			IndexUtils.releaseIndexSearcher(indexType, indexSearcher);
		}
		return new QueryResult<>(topN, objectList);
	}
	
}
