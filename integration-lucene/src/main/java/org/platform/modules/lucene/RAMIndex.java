package org.platform.modules.lucene;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/** 内存索引*/
public class RAMIndex implements IIndex {
	
	private Logger logger = Logger.getLogger(RAMIndex.class);
	
	private IndexWriter indexWriter = null;
	
	private ReferenceManager<IndexSearcher> referenceManager = null;
	
	private Object writeLock = new Object();

	@Override
	public IndexWriter obtainIndexWriter() {
		try {
			synchronized(writeLock) {
				if (null == indexWriter) {
					Directory directory = new RAMDirectory();
					if (IndexWriter.isLocked(directory)) {
						IndexWriter.unlock(directory);
					}
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(VERSION, 
							IndexController.getInstance().obtainAnalyzer(IIndex.ANALYZER_MMSEG4J_MAXWORD));
					indexWriter = new IndexWriter(directory, indexWriterConfig);
				}
			}
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
		return indexWriter;
	}
	
	@Override
	public IndexSearcher obtainIndexSearcher() {
		try {
			if (null != referenceManager) {
				referenceManager.maybeRefresh();
				return referenceManager.acquire();
			}
			if (null == indexWriter) indexWriter = obtainIndexWriter();
			SearcherFactory searcherFactory = new SearcherFactory();
			referenceManager = new SearcherManager(indexWriter, true, searcherFactory);
			return referenceManager.acquire();
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public void closeIndexWriter() {
		try {
			indexWriter.close();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void releaseIndexSearcher(IndexSearcher indexSearcher) {
		try {
			referenceManager.release(indexSearcher);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
	}
}
