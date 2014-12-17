package org.platform.modules.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/** 文件索引*/
public class FSIndex implements IIndex {
	
	private Logger logger = Logger.getLogger(FSIndex.class);

	private IndexWriter indexWriter = null;
	
	private ReferenceManager<IndexSearcher> referenceManager = null;
	
	private Object writeLock = new Object();
	
	private boolean isNearRealTime = false;
	
	private String indexPath = "D:\\develop\\java\\lucene\\indexes";
	
	public FSIndex() {
		
	}
	
	public FSIndex(String indexPath) {
		this.indexPath = indexPath;
	}
	
	public FSIndex(boolean isNearRealTime) {
		this.isNearRealTime = isNearRealTime;
	}

	@Override
	public IndexWriter obtainIndexWriter() {
		try {
			synchronized(writeLock) {
				if (null == indexWriter) {
					File indexFileDir = new File(indexPath);
					if (!indexFileDir.exists()) indexFileDir.mkdirs();
					Directory directory = FSDirectory.open(indexFileDir);
					if (IndexWriter.isLocked(directory)) {
						IndexWriter.unlock(directory);
					}
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(VERSION, 
							IndexUtils.obtainDefaultAnalyzer());
					indexWriter = new IndexWriter(directory, indexWriterConfig);
				}
			}
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		}
		return indexWriter;
	}

	@SuppressWarnings("resource")
	@Override
	public IndexSearcher obtainIndexSearcher() {
		try {
			if (null != referenceManager) {
				if (!isNearRealTime) referenceManager.maybeRefresh();
				return referenceManager.acquire();
			}
			if (null == indexWriter) indexWriter = obtainIndexWriter();
			SearcherFactory searcherFactory = new SearcherFactory();
			referenceManager = new SearcherManager(indexWriter, false, searcherFactory);
			if (isNearRealTime) {
				TrackingIndexWriter trackingIndexWriter = new TrackingIndexWriter(indexWriter);
				ControlledRealTimeReopenThread<IndexSearcher> controlledRealTimeReopenThread = 
						new ControlledRealTimeReopenThread<IndexSearcher>(trackingIndexWriter, referenceManager, 5.0, 0.025);
				controlledRealTimeReopenThread.setName("FSControlledRealTimeReopenThread");
				controlledRealTimeReopenThread.setDaemon(true);
				controlledRealTimeReopenThread.start();
			}
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
