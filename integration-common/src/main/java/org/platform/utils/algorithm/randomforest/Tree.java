package org.platform.utils.algorithm.randomforest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 ** 决策树（非叶结点），决策树中的每个非叶结点都引导了一棵决策树 *
 *  每个非叶结点包含一个分支属性和多个分支，分支属性的每个值对应一个分支，该分支引导了一棵子决策树
 */
public class Tree {
	
	private String attribute;
	private Map<Object, Object> children = new HashMap<Object, Object>();
	
	public Tree() {
		
	}

	public Tree(String attribute) {
		this.attribute = attribute;
	}

	public String getAttribute() {
		return attribute;
	}

	public Object getChild(Object attrValue) {
		return children.get(attrValue);
	}

	public void setChild(Object attrValue, Object child) {
		children.put(attrValue, child);
	}

	public Set<Object> getAttributeValues() {
		return children.keySet();
	}

	public Map<Object, Object> getChildren() {
		return children;
	}
	
	public void tree2json(Tree tree, StringBuilder sb) {
		sb.append("{");
		sb.append("attribute:").append(tree.getAttribute());
		Map<Object, Object> children = tree.getChildren();
		if (children.size() > 0) {
			sb.append(",");
			sb.append("children: {");
			for (Map.Entry<Object, Object> entry : children.entrySet()) {
				sb.append(entry.getKey()).append(":");
				Object value = entry.getValue();
				if (value instanceof String) {
					sb.append(value);
				} else if (value instanceof Tree) {
					tree2json((Tree) value, sb);
				}
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
		} 
		sb.append("}");
	}
	
	
}
