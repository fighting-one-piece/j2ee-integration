package org.platform.modules.lucene.original;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.platform.utils.resource.ResourceUtils;

public class LuceneManagerImpl implements ILuceneManager {
	
	private IndexWriter indexWriter = null;
	
	private IndexReader indexReader = null;
	
	private Object writeLock = new Object();
	
	private Object readLock = new Object();

	@Override
	public IndexWriter getIndexWriter() throws CorruptIndexException,
			LockObtainFailedException, IOException {
		synchronized(writeLock) {
			if (null == indexWriter) {
				Directory directory = FSDirectory.open(new File(ResourceUtils.getAbsolutePath("index")));
				if (IndexWriter.isLocked(directory)) {
					IndexWriter.unlock(directory);
				}
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_45, analyzer);
				indexWriter = new IndexWriter(directory, indexWriterConfig);
			}
		}
		return indexWriter;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IndexReader getIndexReader() throws CorruptIndexException,
			IOException {
		synchronized (readLock) {
			if (null == indexReader) {
				indexReader = IndexReader.open(FSDirectory.open(new File(ResourceUtils.getAbsolutePath("index"))));
			}
		}
		return null;
	}

	@Override
	public void closeIndexWriter() throws IOException {
		if (null != this.indexWriter) {
			this.indexWriter.close();
		}
	}

	@Override
	public void closeIndexReader() throws IOException {
		if (null != this.indexReader) {
			this.indexReader.close();
		}
	}

	@Override
	public void closeAll() throws IOException {
		// TODO Auto-generated method stub

	}

}
