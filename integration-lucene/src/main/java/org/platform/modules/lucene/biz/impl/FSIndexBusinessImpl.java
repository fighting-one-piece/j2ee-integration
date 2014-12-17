package org.platform.modules.lucene.biz.impl;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IndexUtils;

/** 基于文件索引的操作*/
public class FSIndexBusinessImpl extends AbstrIndexBusinessImpl {
	
	private boolean isNearRealTime = false;
	
	public FSIndexBusinessImpl() {
		
	}
	
	public FSIndexBusinessImpl(boolean isNearRealTime) {
		this.isNearRealTime = isNearRealTime;
	}

	@Override
	protected IndexWriter obtainIndexWriter() {
		return IndexUtils.obtainIndexWriter(isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE);
	}

	@Override
	protected IndexSearcher obtainIndexSearcher() {
		return IndexUtils.obtainIndexSearcher(isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE);
	}

	@Override
	protected void releaseIndexSearcher(IndexSearcher indexSearcher) {
		IndexUtils.releaseIndexSearcher(
				isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE, indexSearcher);
	}

}
