package org.platform.modules.abstr.storage;

import redis.clients.jedis.Builder;
import redis.clients.jedis.BuilderFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExtendedBuilderFactory extends BuilderFactory {
	
	public static final Builder<Map<byte[], byte[]>> BYTE_ARRAY_MAP = new Builder<Map<byte[], byte[]>>() {
		
		@SuppressWarnings("unchecked")
		public Map<byte[], byte[]> build(Object data) {
			final List<byte[]> flatHash = (List<byte[]>) data;
			final Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
			final Iterator<byte[]> iterator = flatHash.iterator();
			while (iterator.hasNext()) {
				hash.put(iterator.next(), iterator.next());
			}
			return hash;
		}

		public String toString() {
			return "Map<byte[], byte[]>";
		}

	};
}
