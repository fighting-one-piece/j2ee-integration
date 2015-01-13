package org.platform.modules.abstr.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.platform.modules.abstr.entity.Thing;
import org.platform.modules.abstr.entity.ThingData;
import org.platform.modules.abstr.entity.ThingRelation;
import org.platform.utils.date.DateFormat;

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
			basicAttributes.add("createTimeGT");
			basicAttributes.add("createTimeGE");
			basicAttributes.add("createTimeLT");
			basicAttributes.add("createTimeLE");
			basicAttributes.add("ids");
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
	
	public static String relationTable(Class<?> clazz) {
		return "db_relation_" + clazz.getSimpleName().toLowerCase();
	}
	
	public static boolean isBasicAttribute(String attribute) {
		return basicAttributes.contains(attribute) ? true : false;
	}
	
	public static boolean isNeedUpdate(Thing thing) {
		boolean idFlag = false, fieldFlag = false;
		Field[] fields = Thing.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name) ||
						"table".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(thing);
				field.setAccessible(false);
				if (null == value) continue;
				if ("id".equalsIgnoreCase(name)) {
					idFlag = true;
				} else {
					fieldFlag = true;
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return idFlag && fieldFlag ? true : false;
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
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				Field objField = object.getClass().getSuperclass().getDeclaredField(name);
				if (null == objField) continue;
				objField.setAccessible(true);
				Object objFieldValue = objField.get(object);
				objField.setAccessible(false);
				field.setAccessible(true);
				field.set(thing, objFieldValue);
				field.setAccessible(false);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		thing.setTable(thingTable(object.getClass()));
		return thing;
	}
	
	public static Thing extractSelfThing(Object object) {
		Thing thing = new Thing();
		Field[] fields = Thing.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				int modifier = field.getModifiers();
				if (Modifier.isStatic(modifier) || Modifier.isTransient(modifier)) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				Field objField = object.getClass().getDeclaredField(name);
				if (null == objField) continue;
				objField.setAccessible(true);
				Object objFieldValue = objField.get(object);
				objField.setAccessible(false);
				field.setAccessible(true);
				field.set(thing, objFieldValue);
				field.setAccessible(false);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		thing.setTable(thingTable(object.getClass()));
		return thing;
	}
	
	public static List<ThingData> extractThingDatas(Object object) {
		List<ThingData> thingDatas = new ArrayList<ThingData>();
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			ThingData thingData = null;
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				if (null == value) continue;
				thingData = new ThingData();
				thingData.setTable(dataTable(object.getClass()));
				thingData.setThingId(extractId(object));
				thingData.setAttribute(name);
				String[] valueAndKey = getValueAndKind(value);
				thingData.setValue(valueAndKey[0]);
				thingData.setKind(valueAndKey[1]);
				thingDatas.add(thingData);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return thingDatas;
	}
	
	public static ThingRelation extractThingRelation(Object object) {
		ThingRelation thingRelation = new ThingRelation();
		Field[] fields = ThingRelation.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				Field objField = object.getClass().getSuperclass().getDeclaredField(name);
				if (null == objField) continue;
				objField.setAccessible(true);
				Object objFieldValue = objField.get(object);
				objField.setAccessible(false);
				field.setAccessible(true);
				field.set(thingRelation, objFieldValue);
				field.setAccessible(false);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (Field field : object.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				field.setAccessible(false);
				if (null == fieldValue) continue;
				map.put(name, fieldValue);
			}
			String json = convertMapToJson(map);
			thingRelation.setInfo(json);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		thingRelation.setTable(relationTable(object.getClass()));
		return thingRelation;
	}
	
	public static ThingRelation extractThingRelationNoInfo(Object object) {
		ThingRelation thingRelation = new ThingRelation();
		Field[] fields = ThingRelation.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name)) continue;
				Field objField = object.getClass().getSuperclass().getDeclaredField(name);
				if (null == objField) continue;
				objField.setAccessible(true);
				Object objFieldValue = objField.get(object);
				objField.setAccessible(false);
				field.setAccessible(true);
				field.set(thingRelation, objFieldValue);
				field.setAccessible(false);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		thingRelation.setTable(relationTable(object.getClass()));
		return thingRelation;
	}
	
	public static Object convertThingToObject(Thing thing, Class<?> entityClass) {
		if (null == thing) return null;
		Object entity = null; 
		try {
			Map<String, Object> map = convertObjectToMap(thing);
			for (ThingData thingData : thing.getDatas()) {
				map.put(thingData.getAttribute(), getValueByKind(thingData.getKind(), thingData.getValue()));
			}
			entity = entityClass.newInstance();
			convertMapToObject(map, entity);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return entity;
	}
	
	public static Object convertThingToObject(Thing thing, List<ThingData> thingDatas, Class<?> entityClass) {
		Object entity = null;
		try {
			Map<String, Object> map = convertObjectToMap(thing);
			for (ThingData thingData : thingDatas) {
				map.put(thingData.getAttribute(), getValueByKind(thingData.getKind(), thingData.getValue()));
			}
			entity = entityClass.newInstance();
			convertMapToObject(map, entity);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return entity;
	}
	
	public static Object convertThingRelationToObject(ThingRelation thingRelation, Class<?> entityClass) {
		Object entity = null;
		try {
			if (null == thingRelation) return entity;
			Map<String, Object> map = convertObjectToMap(thingRelation);
			if (null != thingRelation.getInfo()) {
				Map<String, Object> jsonMap = convertJsonToMap(thingRelation.getInfo());
				map.putAll(jsonMap);
			}
			entity = entityClass.newInstance();
			convertMapToObject(map, entity);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return entity;
	}
	
	public static Map<String, Object> convertObjectToMap(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == object) return map;
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				String name = field.getName();
				if ("serialVersionUID".equalsIgnoreCase(name) 
						|| "datas".equalsIgnoreCase(name)) continue;
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				if (null == value) continue;
				map.put(name, value);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return map;
	}
	
	public static void convertMapToObject(Map<String, Object> map, Object object) {
		try {
			for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
	        	for (Field field : superClass.getDeclaredFields()) {
	        		if (Modifier.isStatic(field.getModifiers())) continue;
	        		String name = field.getName();
					if ("serialVersionUID".equalsIgnoreCase(name)) continue;
					Object value = map.get(name);
					if (null == value) continue;
					field.setAccessible(true);
					field.set(object, value);
					field.setAccessible(false);
	    		}
	        }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertJsonToMap(String json) {
		if (json == null || json.length() == 0)
			return new HashMap<String, Object>();
		JSONObject jsonObj = JSONObject.fromObject(json);
		return (Map<String, Object>) JSONObject.toBean(jsonObj, Map.class);
	}
	
	public static String convertMapToJson(Map<String, Object> map){
		if(map == null || map.size() == 0)
			return "";
		String json = JSONObject.fromObject(map).toString();
		return json;
	}
	
	public static Object getValueByFieldName(Object object, String fieldName) {
        Object value = null;
        try {
        	for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            	for (Field field : superClass.getDeclaredFields()) {
            		if(null == field || !fieldName.equals(field.getName())) continue;
			        if (field.isAccessible()) {
			            value = field.get(object);
			        } else {
			            field.setAccessible(true);
			            value = field.get(object);
			            field.setAccessible(false);
			        }
        		}
        	}
        } catch (Exception e) {
        	LOG.info(e.getMessage(), e);
        }
        return value;
    }
	
	public static void setValueByFieldName(Object object, String fieldName, Object value) {
		try {
			for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            	for (Field field : superClass.getDeclaredFields()) {
        			if (fieldName.equals(field.getName())) {
        				field.setAccessible(true);
        				field.set(object, value);
        				field.setAccessible(false);
        			}
        		}
            }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
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
		if (kind.equalsIgnoreCase(Kind.BOOLEAN.getName())) {
    		finalValue = Boolean.parseBoolean(String.valueOf(value));
		} else if (kind.equalsIgnoreCase(Kind.INTEGER.getName())) {
			finalValue = Integer.parseInt(value);
		} else if (kind.equalsIgnoreCase(Kind.LONG.getName())) {
			finalValue = Long.parseLong(value);
		} else if (kind.equalsIgnoreCase(Kind.FLOAT.getName())) {
			finalValue = Float.parseFloat(value);
		} else if (kind.equalsIgnoreCase(Kind.DOUBLE.getName())) {
			finalValue = Double.parseDouble(value);
		} else if (kind.equalsIgnoreCase(Kind.DATE.getName())) {
			try {
				finalValue = DateFormat.TIME.get().parse(value);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return finalValue;
	}
	
	public static String[] getValueAndKind(Object value) {
		String finalValue = String.valueOf(value);
		String kind = Kind.STRING.getName();
		if (value instanceof Boolean) {
			kind = Kind.BOOLEAN.getName();
		} else if (value instanceof Integer) {
			kind = Kind.INTEGER.getName();
		} else if (value instanceof Long) {
			kind = Kind.LONG.getName();
		} else if (value instanceof Float) {
			kind = Kind.FLOAT.getName();
		} else if (value instanceof Double) {
			kind = Kind.DOUBLE.getName();
		} else if (value instanceof Date) {
			finalValue = DateFormat.TIME.get().format((Date) value);
			kind = Kind.DATE.getName();
		} 
		return new String[]{finalValue, kind};
	}
	
}
