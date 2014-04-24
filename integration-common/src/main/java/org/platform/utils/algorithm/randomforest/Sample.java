package org.platform.utils.algorithm.randomforest;

import java.util.HashMap;
import java.util.Map;

/** 
 ** 样本，包含多个属性和一个指明样本所属分类的分类值 
 **/
public class Sample {

	private Object category = null;
	private Map<String, Object> attributes = null;

	public Object getCategory() {
		return category;
	}
	
	public void setCategory(Object category) {
		this.category = category;
	}
	
	public Object getAttribute(String name) {
		return getAttributes().get(name);
	}

	public void setAttribute(String name, Object value) {
		getAttributes().put(name, value);
	}
	
	public Map<String, Object> getAttributes() {
		if (null == attributes) {
			attributes = new HashMap<String, Object>();
		}
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String toString() {
		return attributes.toString();
	}
}
