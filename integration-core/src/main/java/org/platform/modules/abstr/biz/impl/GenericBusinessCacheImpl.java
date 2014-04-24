package org.platform.modules.abstr.biz.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenericBusinessCacheImpl<Entity extends Serializable, PK extends Serializable> 
	extends GenericBusinessImpl<Entity, PK> {
	
	public static final int CACHE_RESOURCES = 0;
	
	@SuppressWarnings("rawtypes")
	private static Map<Integer, GenericBusinessCacheImpl> cacheMap = new HashMap<Integer, GenericBusinessCacheImpl>();
	
	protected void addCacheManage(int cacheType,
			GenericBusinessCacheImpl<Entity, PK> cacheInstance) {
		cacheMap.put(cacheType, cacheInstance);
	}
	
	protected void initCache() {
		
	}
	
	protected void initCache(int cacheType){
		if(null == cacheMap.get(cacheType)) {
			return;
		}
		cacheMap.get(cacheType).initCache();
	}
}
