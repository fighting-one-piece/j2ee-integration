package org.platform.modules.abstr.dao.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.platform.modules.abstr.common.ThingUtils;

public class CQLBuilder {
	
	private static Logger LOG = Logger.getLogger(CQLBuilder.class);

	public static String buildThingCQL(Class<?> clazz, Query query) {
		StringBuilder t_query = new StringBuilder();
		t_query.append("select * from ").append(ThingUtils.thingTable(clazz));
		t_query.append(buildThingConditionCQL(clazz, query));
		LOG.info("t_query: " + t_query.toString());
		return t_query.toString();
	}
	
	public static String buildThingCountCQL(Class<?> clazz, Query query) {
		StringBuilder t_query = new StringBuilder();
		t_query.append("select count(*) from ").append(ThingUtils.thingTable(clazz));
		query.setLimit(Integer.MAX_VALUE);
		t_query.append(buildThingConditionCQL(clazz, query));
		LOG.info("t_query: " + t_query.toString());
		return t_query.toString();
	}
	
	public static String buildThingConditionCQL(Class<?> clazz, Query query) {
		StringBuilder condition = new StringBuilder();
		List<QueryItem> basicAttributes = query.getBasicAttributes();
		boolean isFirstParam = true;
		for (QueryItem item : basicAttributes) {
			if (isFirstParam) {
				condition.append(" where ");
				isFirstParam = false;
			} else {
				condition.append(" and ");
			}
			condition.append(" ").append(item.getConditionKey())
				.append(" ").append(item.getMatch().getSymbol())
				.append(" ").append(wrapper(item.getConditionValue()));
		}
		String orderAttribute = query.getOrderAttribute();
		if (null != orderAttribute) {
			condition.append(" order by ").append(orderAttribute)
				.append(" ").append(query.getOrderType());
		}
		int limit = query.getLimit();
		if (limit != Integer.MAX_VALUE) {
			condition.append(" limit ").append(limit);
		}
		condition.append(" allow filtering ");
		return condition.toString();
	}
	
	public static String buildThingCQL(Class<?> clazz, Map<String, Object> params) {
		StringBuilder t_query = new StringBuilder();
		t_query.append("select * from ").append(ThingUtils.thingTable(clazz));
		boolean isFirstParam = true;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (isFirstParam) {
				t_query.append(" where ");
				isFirstParam = false;
			} else {
				t_query.append(" and ");
			}
			String attribute = entry.getKey();
			Object value = entry.getValue();
			t_query.append(" ").append(attribute).append(" = ").append(value);
		}
		t_query.append(" allow filtering ");
		LOG.info("t_query: " + t_query.toString());
		return t_query.toString();
	}
	
	public static Object[] buildThingCQLAndParams(Class<?> clazz, Map<String, Object> params) {
		StringBuilder t_query = new StringBuilder();
		t_query.append("select * from ").append(ThingUtils.thingTable(clazz));
		List<Object> t_params = new ArrayList<Object>();
		boolean isFirstParam = true;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (isFirstParam) {
				t_query.append(" where ");
				isFirstParam = false;
			} else {
				t_query.append(" and ");
			}
			String attribute = entry.getKey();
			Object value = entry.getValue();
			t_query.append(" ").append(attribute).append(" = ? ");
			t_params.add(value);
		}
		t_query.append(" allow filtering ");
		LOG.info("t_query: " + t_query.toString());
		return new Object[]{t_query.toString(), t_params.toArray(new Object[0])};
	}
	
	public static String buildOneConditionDataCQL(Class<?> clazz, Query query) {
		StringBuilder d_query = new StringBuilder();
		d_query.append("select * from ").append(ThingUtils.dataTable(clazz));
		List<QueryItem> dataAttributes = query.getDataAttributes();
		boolean isFirstParam = true;
		for (QueryItem item : dataAttributes) {
			if (isFirstParam) {
				d_query.append(" where ");
				isFirstParam = false;
			} else {
				d_query.append(" and ");
			}
			String attribute = item.getConditionKey();
			Object value = item.getConditionValue();
			if (attribute.equalsIgnoreCase("thingId")) {
				d_query.append(" thing_id = ").append(value).append(" ");
			} else {
				d_query.append(" key = '").append(attribute).append("' and ");
				d_query.append(" value = '").append(value).append("' ");
			}
		}
		d_query.append(" allow filtering ");
		LOG.info("d_query: " + d_query.toString());
		return d_query.toString();
	}
	
	public static List<String> buildMultiConditionDataCQL(Class<?> clazz, Query query) {
		List<QueryItem> dataAttributes = query.getDataAttributes();
		StringBuilder whereClause = new StringBuilder(" where ");
		StringBuilder idClause = new StringBuilder();
		for (QueryItem item : dataAttributes) {
			String attribute = item.getConditionKey();
			Object value = item.getConditionValue();
			if (attribute.equalsIgnoreCase("thingId")) {
				idClause.append(" thing_id = ").append(value).append(" ");
				break;
			}
		}
		whereClause.append(idClause);
		StringBuilder selectClause = new StringBuilder();
		selectClause.append("select * from ").append(ThingUtils.dataTable(clazz));
		List<String> cqls = new ArrayList<String>();
		for (QueryItem item : dataAttributes) {
			String attribute = item.getConditionKey();
			Object value = item.getConditionValue();
			if (attribute.equalsIgnoreCase("thingId")) continue;
			StringBuilder cql = new StringBuilder(selectClause);
			cql.append(whereClause);
			if (idClause.length() > 0) {
				cql.append(" and ");
			}
			cql.append(" key = '").append(attribute).append("' and ");
			cql.append(" value = '").append(value).append("' ");
			cql.append(" allow filtering ");
			cqls.add(cql.toString());
			LOG.info("d_cql: " + cql.toString());
		}
		if (cqls.size() == 0 && idClause.length() > 0) {
			StringBuilder cql = new StringBuilder(selectClause);
			cql.append(whereClause);
			cql.append(" allow filtering ");
			cqls.add(cql.toString());
			LOG.info("d_cql: " + cql.toString());
		}
		return cqls;
	}
	
	public static String buildDataCQL(Class<?> clazz, Map<String, Object> params) {
		StringBuilder d_query = new StringBuilder();
		d_query.append("select * from ").append(ThingUtils.dataTable(clazz));
		boolean isFirstParam = true;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (isFirstParam) {
				d_query.append(" where ");
				isFirstParam = false;
			} else {
				d_query.append(" and ");
			}
			String attribute = entry.getKey();
			Object value = entry.getValue();
			if (attribute.equalsIgnoreCase("thingId")) {
				d_query.append(" thing_id = ").append("value").append(" ");
			} else {
				d_query.append(" key = ").append(attribute).append(" and ");
				d_query.append(" value = ").append(value).append(" ");
			}
		}
		d_query.append(" allow filtering ");
		LOG.info("d_query: " + d_query.toString());
		return d_query.toString();
	}
	
	public static Object[] buildDataCQLAndParams(Class<?> clazz, Map<String, Object> params) {
		StringBuilder d_query = new StringBuilder();
		d_query.append("select * from ").append(ThingUtils.dataTable(clazz));
		List<Object> d_params = new ArrayList<Object>();
		boolean isFirstParam = true;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (isFirstParam) {
				d_query.append(" where ");
				isFirstParam = false;
			} else {
				d_query.append(" and ");
			}
			String attribute = entry.getKey();
			Object value = entry.getValue();
			if (attribute.equalsIgnoreCase("thingId")) {
				d_query.append(" thing_id = ? ");
				d_params.add(value);
			} else {
				d_query.append(" key = ? and value = ? ");
				d_params.add(attribute);
				d_params.add(value);
			}
		}
		d_query.append(" allow filtering ");
		LOG.info("d_query: " + d_query.toString());
		return new Object[]{d_query.toString(), d_params.toArray(new Object[0])};
	}
	
	public static String wrapper(Object value) {
		String result = String.valueOf(value);
		if (value instanceof String) {
			result = "'" + result + "'";
		}
		return result;
	}
	
	public static void main(String[] args) {
	}
}
