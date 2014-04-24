package org.platform.modules.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

/** 基于文件索引的操作*/
public class FSIndexManager extends AbstrIndexManager {
	
	private boolean isNearRealTime = false;
	
	public FSIndexManager() {
		
	}
	
	public FSIndexManager(boolean isNearRealTime) {
		this.isNearRealTime = isNearRealTime;
	}

	@Override
	protected IndexWriter obtainIndexWriter() {
		return IndexController.getInstance().obtainIndexWriter(isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE);
	}

	@Override
	protected IndexSearcher obtainIndexSearcher() {
		return IndexController.getInstance().obtainIndexSearcher(isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE);
	}

	@Override
	protected void releaseIndexSearcher(IndexSearcher indexSearcher) {
		IndexController.getInstance().releaseIndexSearcher(
				isNearRealTime ? IIndex.FILE_NRT : IIndex.FILE, indexSearcher);
	}

}
