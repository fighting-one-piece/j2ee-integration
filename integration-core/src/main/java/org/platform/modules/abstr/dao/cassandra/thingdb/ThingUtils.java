package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.cassandra.mapping.Column;

public class ThingUtils {
	
	public static Logger LOG = Logger.getLogger(ThingUtils.class);
	
	private static Set<String> basicAttributes = null;
	
	static {
		basicAttributes = new HashSet<String>();
		Field[] fields = Thing.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				basicAttributes.add(name);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	public static String thingTable(Class<?> clazz) {
		return "db_thing_" + clazz.getSimpleName().toLowerCase();
	}
	
	public static String dataTable(Class<?> clazz) {
		return "db_data_" + clazz.getSimpleName().toLowerCase();
	}
	
	public static boolean isBasicAttribute(String attribute) {
		return basicAttributes.contains(attribute) ? true : false;
	}
	
	public static Long extractId(Object object) {
		Field[] superFields = object.getClass().getSuperclass().getDeclaredFields();
		try {
			for (Field field : superFields) {
				String name = field.getName();
				if (!"id".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				return (Long) value;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return null;
	}
	
	public static Thing extractThing(Object object) {
		Thing thing = new Thing();
		Field[] fields = Thing.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				Field objField = object.getClass().getDeclaredField(name);
				if (null == objField) continue;
				objField.setAccessible(true);
				Object objValue = objField.get(object);
				objField.setAccessible(false);
				field.setAccessible(true);
				field.set(thing, objValue);
				field.setAccessible(false);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return thing;
	}
	
	public static List<ThingData> extractThingDatas(Object object) {
		List<ThingData> thingDatas = new ArrayList<ThingData>();
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			ThingData thingData = null;
			for (Field field : fields) {
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				if (null == value) continue;
				System.out.println(name + " : " + value);
				thingData = new ThingData();
				thingData.setThingId(extractId(object));
				thingData.setKey(name);
				thingData.setValue(String.valueOf(value));
				thingData.setKind(getKindByValue(value));
				thingDatas.add(thingData);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return thingDatas;
	}
	
	//flag true is column name false is field name
	public static Map<String, Object> convertObjectToMap(Object object, boolean flag) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				if (null == value) continue;
				String key = name;
				if (flag) {
					String columnName = getColumnName(field);
					if (null != columnName) {
						key = columnName;
					}
				}
				System.out.println(key + " : " + value);
				map.put(key, value);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return map;
	}
	
	public static String getColumnName(Field field) {
		Column column = field.getAnnotation(Column.class);
		return null != column ? column.value() : null;
	}
	
	public static void convertMapToObject(Map<String, Object> map, Object object) {
		try {
			Method[] methods = object.getClass().getMethods();
			for (Method method : methods) {
				String mname = method.getName();
				if (!mname.startsWith("set")) continue;
				StringBuffer sb = new StringBuffer();
				sb.append(String.valueOf(mname.charAt(3)).toLowerCase());
				sb.append(mname.substring(4));
				String name = sb.toString();
				if (null != map.get(name)) {
					method.invoke(object, map.get(name));
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	public static Object dataToObject(Thing thing, List<ThingData> thingDatas, Class<?> entityClass) {
		Object entity = null;
		try {
			entity = entityClass.newInstance();
			Map<String, Object> tmap = convertObjectToMap(thing, false);
			for (ThingData thingData : thingDatas) {
				tmap.put(thingData.getKey(), thingData.getValue());
			}
			Method[] methods = entityClass.getMethods();
			for (Method method : methods) {
				String mname = method.getName();
				if (!mname.startsWith("set")) continue;
				StringBuffer sb = new StringBuffer();
				sb.append(String.valueOf(mname.charAt(3)).toLowerCase());
				sb.append(mname.substring(4));
				String name = sb.toString();
				if (null != tmap.get(name)) {
					method.invoke(entity, tmap.get(name));
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return entity;
	}
	
	public static void setValueBySetMethod(Object object, Object value, String methodName, Class<?>... parameterTypes) {
		try {
			object.getClass().getMethod(methodName, parameterTypes).invoke(object, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	public static void setValuesBySetMethod(Object object, Object values[], String methodName, Class<?>... parameterTypes) {
		try {
			object.getClass().getMethod(methodName, parameterTypes).invoke(object, values);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	public static Object getValueByMethodName(Object object, String methodName) {
		return getValueByMethodName(object, new Object[0], methodName, new Class[0]);
	}
	
	public static Object getValueByMethodName(Object object, Object values[], String methodName, Class<?>... parameterTypes) {
		Object returnValue = null;
		try {
			returnValue = object.getClass().getMethod(methodName, parameterTypes).invoke(object, values);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return returnValue;
	}
	
	public static Object getValueByKind(String kind, String value) {
		Object finalValue = value;
		if (kind.equalsIgnoreCase(Kind.INTEGER.getName())) {
			finalValue = Integer.parseInt(value);
		} else if (kind.equalsIgnoreCase(Kind.LONG.getName())) {
			finalValue = Long.parseLong(value);
		} else if (kind.equalsIgnoreCase(Kind.FLOAT.getName())) {
			finalValue = Float.parseFloat(value);
		} else if (kind.equalsIgnoreCase(Kind.DOUBLE.getName())) {
			finalValue = Double.parseDouble(value);
		} else if (kind.equalsIgnoreCase(Kind.DATE.getName())) {
			try {
				finalValue = DateFormat.timeFormat.get().parse(value);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return finalValue;
	}
	
	public static String getKindByValue(Object value) {
		String kind = Kind.STRING.getName();
		if (value instanceof Integer) {
			kind = Kind.INTEGER.getName();
		} else if (value instanceof Long) {
			kind = Kind.LONG.getName();
		} else if (value instanceof Float) {
			kind = Kind.FLOAT.getName();
		} else if (value instanceof Double) {
			kind = Kind.DOUBLE.getName();
		} else if (value instanceof Date) {
			kind = Kind.DATE.getName();
		} 
		return kind;
	}
	
	public static void main(String[] args) throws Exception {
	}
	
}
