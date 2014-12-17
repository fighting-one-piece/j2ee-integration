package org.platform.modules.lucene.biz.impl;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IndexUtils;

/** 基于内存的索引操作*/
public class RAMIndexBusinessImpl extends AbstrIndexBusinessImpl {

	@Override
	protected IndexWriter obtainIndexWriter() {
		return IndexUtils.obtainIndexWriter(IIndex.RAM);
	}

	@Override
	protected IndexSearcher obtainIndexSearcher() {
		return IndexUtils.obtainIndexSearcher(IIndex.RAM);
	}

	@Override
	protected void releaseIndexSearcher(IndexSearcher indexSearcher) {
		IndexUtils.releaseIndexSearcher(IIndex.RAM, indexSearcher);
	}

}
