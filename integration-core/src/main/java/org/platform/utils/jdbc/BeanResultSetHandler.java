package org.platform.utils.jdbc;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import javax.persistence.Column;

import org.platform.utils.reflect.ReflectUtils;

public class BeanResultSetHandler extends AbstrResultSetHandler {
	
	@SuppressWarnings("unused")
	private BeanResultSetHandler() {
		
	}
	
	public BeanResultSetHandler(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	protected Object doHandle(ResultSet resultSet) {
		Object object = null;
		try{
			if (!resultSet.first()) return object;
			object = clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(Column.class)) continue;
				String columnLabel = field.getAnnotation(Column.class).name();
				Object columnValue = resultSet.getObject(columnLabel);
				if (null == columnValue) continue;
				ReflectUtils.setValueByFieldName(object, field.getName(), columnValue);
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
		return object;
	}
}
