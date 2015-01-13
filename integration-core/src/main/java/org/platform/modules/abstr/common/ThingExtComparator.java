package org.platform.modules.abstr.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ThingExtComparator implements Comparator<Object> {
	
	private List<Sort> sorts = null;
	
	public ThingExtComparator(Sort sort) {
		sorts = new ArrayList<Sort>();
		sorts.add(sort);
	}
	
	public ThingExtComparator(List<Sort> sorts) {
		this.sorts = sorts;
	}

	@Override
	public int compare(Object o1, Object o2) {
		for (Sort sort : sorts) {
			Object v1 = ThingUtils.getValueByMethodName(o1, sort.getName());
			Object v2 = ThingUtils.getValueByMethodName(o2, sort.getName());
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
