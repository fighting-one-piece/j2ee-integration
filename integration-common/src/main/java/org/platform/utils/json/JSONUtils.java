package org.platform.utils.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

public class JSONUtils {

	public static String object2json(Object object) {
		StringBuffer sb = new StringBuffer();
		if (null == object)
			sb.append("\"\"");
		else
			sb.append(object2json(object, null));
		return sb.toString();
	}
	
	public static String object2json(Object object, final String[] properties){
		if (null == properties || properties.length == 0) {
			return Collection.class.isAssignableFrom(object.getClass()) ?
						JSONArray.fromObject(object).toString() :
							JSONObject.fromObject(object).toString();
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setIgnoreDefaultExcludes(false);
//		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				for(String property : properties){
					if(property.equals(arg1)){
						return true;
					}
				}
				return false;
			}
		});
		return Collection.class.isAssignableFrom(object.getClass()) ?
					JSONArray.fromObject(object, jsonConfig).toString() :
						JSONObject.fromObject(object, jsonConfig).toString();
	}

	public static String map2json(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (map != null && map.size() > 0) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				sb.append(entry.getKey());
				sb.append(":");
				sb.append(entry.getValue());
				sb.append(",");
			}
			sb.setCharAt(sb.length() - 1, '}');
		} else {
			sb.append("}");
		}
		return sb.toString();
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case 34: // '"'
				sb.append("\\\"");
				break;
			case 92: // '\\'
				sb.append("\\\\");
				break;
			case 8: // '\b'
				sb.append("\\b");
				break;
			case 12: // '\f'
				sb.append("\\f");
				break;
			case 10: // '\n'
				sb.append("\\n");
				break;
			case 13: // '\r'
				sb.append("\\r");
				break;
			case 9: // '\t'
				sb.append("\\t");
				break;
			case 47: // '/'
				sb.append("\\/");
				break;
			default:
				if (ch >= 0 && ch <= '\037') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++)
						sb.append('0');
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
				break;
			}
		}
		return sb.toString();
	}

	public static JSONObject json2Object(String jsonData) {
		return JSONObject.fromObject(jsonData);
	}
	
	public static Object json2Object(String jsonData, Class<?> clazz) {
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		Object object = null;
		try {
			object = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				String name = field.getName();
				if (!jsonObject.containsKey(name)) {
					continue;
				}
				field.getType();
				field.setAccessible(true);
				field.set(object, jsonObject.get(name));
				field.setAccessible(false);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}

}
