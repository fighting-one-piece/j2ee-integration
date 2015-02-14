package org.platform.modules.lucene.biz.impl;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.store.Directory;
import org.platform.entity.PKEntity;
import org.platform.entity.QueryResult;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IndexUtils;
import org.platform.modules.lucene.biz.IIndexBusiness;
import org.platform.modules.lucene.entity.QueryCondition;
import org.platform.utils.reflect.ReflectUtils;
import org.platform.utils.word.WordUtils;

public abstract class AbstrIndexBusinessImpl implements IIndexBusiness {

	protected Logger LOG = Logger.getLogger(getClass());
	
	protected IndexWriter obtainIndexWriter() {
		return null;
	}
	
	protected IndexSearcher obtainIndexSearcher() {
		return null;
	}
	
	protected void releaseIndexSearcher(IndexSearcher indexSearcher) {
	}
	
	@Override
	public void insert(Object... objects) {
		try {
			List<Document> documents = new ArrayList<Document>();
			for (Object object : objects) {
				documents.add(object2Document(object));
			}
			obtainIndexWriter().addDocuments(documents);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void update(Term term, Object object) {
		try {
			obtainIndexWriter().updateDocument(term, object2Document(object));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Term term) {
		try {
			obtainIndexWriter().deleteDocuments(term);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			obtainIndexWriter().deleteAll();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void commit() {
		try {
			obtainIndexWriter().commit();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void merge(Directory... directories) {
		IndexWriter indexWriter = obtainIndexWriter();
		try {
			indexWriter.addIndexes(directories);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
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
		int topN = Math.max(currentPageNum * rowNumPerPage, rowNumPerPage);
		List<Object> objectList = new ArrayList<Object>();
		IndexSearcher indexSearcher = obtainIndexSearcher();
		try {
			TopDocs topDocs = readDataList(indexSearcher, query, filter, topN, sort);
			topN = null == topDocs ? 0 : topDocs.totalHits;
			if (currentPageNum > 1) {
				int prePageIndexNum = (currentPageNum - 1) * rowNumPerPage - 1;
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				ScoreDoc prePageLastScoreDoc = scoreDocs[prePageIndexNum];
				int n = Math.min(rowNumPerPage, topDocs.totalHits);
				topDocs = readAfterDataList(indexSearcher, prePageLastScoreDoc, query, filter, n, sort);
			}
			if (null != topDocs && null != topDocs.scoreDocs) {
				Analyzer analyzer = condition.getAnalyzer();
				if (null == analyzer) analyzer = IndexUtils.obtainDefaultAnalyzer();
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					LOG.info(scoreDoc.doc + " score: " + scoreDoc.score);
					Document document = indexSearcher.doc(scoreDoc.doc);
					Object object = document2Object(document, condition.getEntityClass());
					highLighter(analyzer, query, document, object, highLighterFields);
					objectList.add(object);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			releaseIndexSearcher(indexSearcher);
		}
		return new QueryResult<>(topN, objectList);
	}
	
	protected Document object2Document(Object object) {
		Document document = new Document();
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			Field field = fields[i];
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
				} else if (Byte.class.isAssignableFrom(fieldType)) {
					document.add(new StringField(fieldName, String.valueOf(fieldValue), Store.YES));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return document;
	}

	protected Object document2Object(Document document, Class<?> clazz) {
		Object object = null;
		try {
			object = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0, len = fields.length; i < len; i++) {
				Field field = fields[i];
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
				} else if (Byte.class.isAssignableFrom(fieldType)) {
					field.set(object, indexableField.stringValue());
				} 
			}
		} catch (IllegalAccessException | InstantiationException e) {
			LOG.error(e.getMessage(), e);
		}
		return object;
	}
	
	protected Query obtainQuery(QueryCondition condition) {
		String[] queryFields = condition.getQueryFields();
		if (null == queryFields) {
			Field[] fields = ReflectUtils.getFields(condition.getEntityClass());
			queryFields = new String[fields.length];
			for (int i = 0, length = fields.length; i < length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				Class<?> type = field.getType();
				if (Collection.class.isAssignableFrom(type)) continue;
				queryFields[i] = field.getName();
				field.setAccessible(false);
			}
		}
		BooleanQuery orQuery = new BooleanQuery();
		String keyword = condition.getKeyword();
		String[] words = StringUtils.isBlank(keyword) ? new String[]{"*"} : WordUtils.splitBySeg(keyword);
		for (int i = 0, wLen = words.length; i < wLen; i++) {
			for (int j = 0, fLen = queryFields.length; j < fLen; j++) {
				String wildcardWord = "*" + words[i] + "*";
				orQuery.add(new WildcardQuery(new Term(queryFields[j], wildcardWord)), 
						BooleanClause.Occur.SHOULD);
			}
		}
		String queryTag = condition.getQueryTag();
		if (StringUtils.isBlank(queryTag)) return orQuery;
		BooleanQuery andQuery = new BooleanQuery();
		andQuery.add(new TermQuery(new Term("search", queryTag)), BooleanClause.Occur.MUST);
		andQuery.add(orQuery, BooleanClause.Occur.MUST);
		return andQuery;
	}
	
	protected Query obtainQuery(Class<?> clazz, String keyword) {
		try {
			List<String> fields = new ArrayList<String>();
			for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				if (Collection.class.isAssignableFrom(type) || PKEntity.class.isAssignableFrom(type)) {
					continue;
				} 
				fields.add(field.getName());
			}
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
					IIndex.VERSION, fields.toArray(new String[0]), IndexUtils.obtainDefaultAnalyzer());
			return queryParser.parse(keyword);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public Sort obtainSort(QueryCondition condition) {
		if (null != condition.getSort()) return condition.getSort();
		SortField[] sortFields = new SortField[]{new SortField(null, SortField.Type.SCORE, true),
				new SortField("createTime", SortField.Type.LONG, true)};
		return new Sort(sortFields);
	}
	
	protected TopDocs readDataList(IndexSearcher indexSearcher, Query query, Filter filter, 
			int topN, Sort sort) throws IOException {
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
	
	protected TopDocs readAfterDataList(IndexSearcher indexSearcher, ScoreDoc scoreDoc, Query query, 
			Filter filter, int n, Sort sort) throws IOException {
		TopDocs topDocs = null;
		if (null == filter && null == sort) {
			topDocs = indexSearcher.searchAfter(scoreDoc, query, n);
		} else if (null != filter && null == sort) {
			topDocs = indexSearcher.searchAfter(scoreDoc, query, filter, n);
		} else if (null == filter && null != sort) {
			topDocs = indexSearcher.searchAfter(scoreDoc, query, n, sort);
		} else if (null != filter && null != sort) {
			topDocs = indexSearcher.searchAfter(scoreDoc, query, filter, n, sort);
		}
		return topDocs;
	}
	
	protected void highLighter(Analyzer analyzer, Query query, Document document, Object object, String... highLighterFields) {
		if (null == highLighterFields || highLighterFields.length == 0 || null == document || null == object) {
			return;
		}
		QueryScorer scorer = new QueryScorer(query);
		Formatter fmt = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter highLighter = new Highlighter(fmt, scorer);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		highLighter.setTextFragmenter(fragmenter);
		try {
			for (String highLighterField : highLighterFields) {
				Field field = ReflectUtils.getFieldByFieldName(object, highLighterField);
				if (null == field) continue;
				IndexableField indexableField = document.getField(highLighterField);
				Class<?> fieldType = field.getType();
				if (!String.class.isAssignableFrom(fieldType)) continue;
				String highLighterText = highLighter.getBestFragments(
						indexableField.tokenStream(analyzer), indexableField.stringValue(), 5, "......\n");
				if (null == highLighterText || "".equals(highLighterText)) continue;
				ReflectUtils.setValueByFieldName(object, highLighterField, 
						null == highLighterText ? indexableField.stringValue() : highLighterText);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	protected void highLighterFast(IndexSearcher indexSearcher, Query query, int docId, Object object, String... highLighterFields) {
		if (null == highLighterFields || highLighterFields.length == 0 || null == object) {
			return;
		}
		FragListBuilder fragListBuilder = new SimpleFragListBuilder();  
		//注意下面的构造函数里，使用的是颜色数组，用来支持多种颜色高亮
		FragmentsBuilder fragmentsBuilder= new ScoreOrderFragmentsBuilder(
				BaseFragmentsBuilder.COLORED_PRE_TAGS, BaseFragmentsBuilder.COLORED_POST_TAGS);  
		FastVectorHighlighter fastHighlighter = new FastVectorHighlighter(true, true, fragListBuilder, fragmentsBuilder);  
		FieldQuery fieldQuery = fastHighlighter.getFieldQuery(query);
		try {
			for (String highLighterField : highLighterFields) {
				String highLighterText = fastHighlighter.getBestFragment(
						fieldQuery, indexSearcher.getIndexReader(), docId, highLighterField, 300);
				if (null == highLighterText || "".equals(highLighterText)) continue;
				ReflectUtils.setValueByFieldName(object, highLighterField, highLighterText);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	protected String obtainHighLighterText(Analyzer analyzer, Query query, String highLighterField, String highLighterFieldValue) {
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		Formatter fmt = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter highLighter = new Highlighter(fmt, scorer);
		highLighter.setTextFragmenter(fragmenter);
		String highLighterTxt = null;
		try {
			highLighterTxt = highLighter.getBestFragments(
						analyzer.tokenStream(highLighterField, new StringReader(highLighterFieldValue)), 
						highLighterFieldValue, 3, "......\n");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return null == highLighterTxt ? highLighterFieldValue : highLighterTxt;
	}
}
