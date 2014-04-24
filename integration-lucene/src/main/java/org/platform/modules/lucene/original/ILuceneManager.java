package org.platform.modules.lucene.original;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;

public interface ILuceneManager {
	
	/**
	 * 获取IndexWriter
	 * @return
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public IndexWriter getIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException;
	
	/**
	 * 获取IndexReader
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public IndexReader getIndexReader() throws CorruptIndexException, IOException;
	
	/**
	 * 关闭IndexWriter
	 * @throws IOException
	 */
	public void  closeIndexWriter() throws IOException;
	
	/**
	 * 关闭IndexReader
	 * @throws IOException
	 */
	public  void closeIndexReader() throws IOException;
	
	/**
	 * 关闭所有
	 * @throws IOException
	 */
	public  void closeAll() throws IOException;

}
