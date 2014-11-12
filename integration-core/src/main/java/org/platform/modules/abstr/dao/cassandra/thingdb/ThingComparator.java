package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ThingComparator implements Comparator<Thing> {
	
	private List<Sort> sorts = null;
	
	public ThingComparator(List<Sort> sorts) {
		this.sorts = sorts;
	}

	@Override
	public int compare(Thing t1, Thing t2) {
		for (Sort sort : sorts) {
			Object v1 = ThingUtils.getValueByMethodName(t1, sort.getName());
			Object v2 = ThingUtils.getValueByMethodName(t2, sort.getName());;
			if (!v1.equals(v2)) {
				if (v1 instanceof Double && v2 instanceof Double) {
					return sort.getOrder() == Sort.ASC ? ((Double) v1).compareTo((Double) v2)
							: ((Double) v2).compareTo((Double) v1);
				} else if (v1 instanceof Date && v2 instanceof Date) {
					return sort.getOrder() == Sort.ASC ? ((Date) v1).compareTo((Date) v2)
							: ((Date) v2).compareTo((Date) v1);
				}
			}
		}
		return 0;
	}

}
