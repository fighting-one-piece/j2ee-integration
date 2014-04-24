package org.platform.modules.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

/** 基于内存的索引操作*/
public class RAMIndexManager extends AbstrIndexManager {

	@Override
	protected IndexWriter obtainIndexWriter() {
		return IndexController.getInstance().obtainIndexWriter(IIndex.RAM);
	}

	@Override
	protected IndexSearcher obtainIndexSearcher() {
		return IndexController.getInstance().obtainIndexSearcher(IIndex.RAM);
	}

	@Override
	protected void releaseIndexSearcher(IndexSearcher indexSearcher) {
		IndexController.getInstance().releaseIndexSearcher(IIndex.RAM, indexSearcher);
	}

}
