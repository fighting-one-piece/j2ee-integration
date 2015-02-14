package org.platform.modules.abstr.web.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.platform.utils.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullUtil {
	
	private static Logger LOG = LoggerFactory.getLogger(NullUtil.class);

	public static boolean isPrimitiveV1(Object object) {
		try {
			Object value = ReflectUtils.getValueByFieldName(object, "TYPE");
			if (null != value) return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static void removeNull(Object object) {  
		if (object instanceof Collection) {
			Collection<Object> collection = (Collection<Object>) object;
			for (Object obj : collection) {
				handleNull(obj);
			}
		} else if (object instanceof Map) { 
			
		} else {
			handleNull(object);
		}
    }  
	
	public static void handleNull(Object object) {
		if (isPrimitive(object)) return;
		try {
			Field[] fields = ReflectUtils.getFields(object);
			for (int i = 0, len = fields.length; i < len; i++) {
				Field field = fields[i];
				Class<?> fieldType = field.getType();
				if (fieldType.isPrimitive()) continue;
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (null != fieldValue) {
					String packageName = fieldValue.getClass().getPackage().getName();
					if (!packageName.startsWith("com.netease.talk")
							&& !Collection.class.isAssignableFrom(fieldType)) {
						continue;
					}
					removeNull(fieldValue);
				}
				Object initialValue = initialValue(fieldType);
				if (null == initialValue) {
				} else {
					field.set(object, initialValue);
				}
				field.setAccessible(false);
			}
    	} catch (Exception e) {
        	LOG.info(e.getMessage(), e);
        }
	}
	
	public static boolean isPrimitive(Object object) {
		if (object instanceof String ||object instanceof Integer 
				||object instanceof Long || object instanceof Float 
					||object instanceof Double || object instanceof Date) {
			return true;
		} 
		return false;
	}
	
	public static Object initialValue(Class<?> type) {
		Object finalValue = null;
    	if (String.class.isAssignableFrom(type)) {
    		finalValue = "";
		} else if (Boolean.class.isAssignableFrom(type)) {
			finalValue = new Boolean(false);
		} else if (Integer.class.isAssignableFrom(type)) {
    		finalValue = new Integer(0);
		} else if (Long.class.isAssignableFrom(type)) {
			finalValue = new Long(0);
		} else if (Float.class.isAssignableFrom(type)) {
			finalValue = new Float(0);
		} else if (Double.class.isAssignableFrom(type)) {
			finalValue = new Double(0);
		} else if (Date.class.isAssignableFrom(type)) {
			finalValue = new Date();
		} 
    	return finalValue;
	}
	
	
	
}
