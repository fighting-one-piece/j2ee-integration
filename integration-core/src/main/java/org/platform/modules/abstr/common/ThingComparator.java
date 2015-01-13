package org.platform.modules.abstr.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.platform.modules.abstr.entity.Thing;

public class ThingComparator implements Comparator<Object> {
	
	private List<Sort> sorts = null;
	
	public ThingComparator(Sort sort) {
		sorts = new ArrayList<Sort>();
		sorts.add(sort);
	}
	
	public ThingComparator(List<Sort> sorts) {
		this.sorts = sorts;
	}

	@Override
	public int compare(Object o1, Object o2) {
		Thing thing1 = ThingUtils.extractSelfThing(o1);
		Thing thing2 = ThingUtils.extractSelfThing(o2);
		for (Sort sort : sorts) {
			Object v1 = ThingUtils.getValueByMethodName(thing1, sort.getName());
			Object v2 = ThingUtils.getValueByMethodName(thing2, sort.getName());
			if (!v1.equals(v2)) {
				boolean isAsc = sort.getOrder() == Sort.ASC ? true : false;
				if (v1 instanceof Double && v2 instanceof Double) {
					return isAsc ? ((Double) v1).compareTo((Double) v2)
							: ((Double) v2).compareTo((Double) v1);
				} else if (v1 instanceof Integer && v2 instanceof Integer) {
					return isAsc ? ((Integer) v1).compareTo((Integer) v2)
							: ((Integer) v2).compareTo((Integer) v1);
				} else if (v1 instanceof Long && v2 instanceof Long) {
					return isAsc ? ((Long) v1).compareTo((Long) v2)
							: ((Long) v2).compareTo((Long) v1);
				}	else if (v1 instanceof Date && v2 instanceof Date) {
					return isAsc ? ((Date) v1).compareTo((Date) v2)
							: ((Date) v2).compareTo((Date) v1);
				}
			}
		}
		return 0;
	}

}
