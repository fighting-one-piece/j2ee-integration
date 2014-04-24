package org.platform.utils.algorithm.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeNode {
	
	/** 节点名称(分裂属性的名称)*/
	private String name = null;
	/** 结点的分裂类型列表*/
	private Set<String> categories = null;
	/** 子结点集合*/
	private Set<TreeNode> children = null;
	/** 划分到该结点的训练元组*/
	private List<List<String>> datas = null; 
	/** 划分到该结点的候选属性*/
	private List<String> candidateAttributes = null;

	public Set<TreeNode> getChildren() {
		if (null == children) {
			children = new HashSet<TreeNode>();
		}
		return children;
	}

	public void setChildren(Set<TreeNode> child) {
		this.children = child;
	}

	public Set<String> getCategories() {
		if (null == categories) {
			categories = new HashSet<String>();
		}
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<List<String>> getDatas() {
		if (null == datas) {
			datas = new ArrayList<List<String>>();
		}
		return datas;
	}

	public void setDatas(List<List<String>> datas) {
		this.datas = datas;
	}

	public List<String> getCandidateAttributes() {
		if (null == candidateAttributes) { 
			candidateAttributes = new ArrayList<String>(); 
		} 
		return candidateAttributes;
	}

	public void setCandidateAttributes(List<String> candAttr) {
		this.candidateAttributes = candAttr;
	}
	
	/**
	 * 获取数据集中的分类及其计数
	 * @return 分类及其计数的map
	 */
	public Map<String, Integer> getDatasClassificationCountMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String key = "";
		List<String> tuple = null;
		for (int i = 0, len = datas.size(); i < len; i++) {
			tuple = datas.get(i);
			key = tuple.get(tuple.size() - 1);
			map.put(key, map.containsKey(key) ? map.get(key) + 1 : 1);
		}
		return map;
	}
	
	/**
	 * 获取数据集中的最大分类，即求多数类
	 * @param map 分类的键值集合
	 * @return 多数类的类名
	 */
	public String getDatasMaxClassification() {
		Map<String, Integer> classificationCountMap = getDatasClassificationCountMap();
		return getDatasMaxClassification(classificationCountMap);
	}
	
	/**
	 * 获取数据集中的最大分类，即求多数类
	 * @param classificationCountMap 分类的键值集合
	 * @return 多数类的类名
	 */
	public String getDatasMaxClassification(Map<String, Integer> classificationCountMap) {
		String maxClassification = "";
		int max = -1;
		for (Map.Entry<String, Integer> entry : classificationCountMap.entrySet()) {
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			if (value > max) {
				max = value;
				maxClassification = key;
			}
		}
		return maxClassification;
	}
	
	/** 
     * 获取最佳侯选属性列上的值域（假定所有属性列上的值都是有限的名词或分类类型的） 
     * @param attrIndex 指定的属性列的索引 
     * @return 值域集合 
     */  
    public Set<String> getDatasCategoriesWithIndex(int attrIndex){  
        Set<String> values = new HashSet<String>();  
        for (int i = 0, len = datas.size(); i < len; i++) {  
            values.add(datas.get(i).get(attrIndex));
        }  
        return values;  
    } 
    
    /** 
     * 获取指定属性列上指定值域的所有元组 
     * @param attrIndex 指定属性列索引 
     * @param value 指定属性列的值域 
     * @return 指定属性列上指定值域的所有元组 
     */  
	public List<List<String>> getDatasWithIndexAndValue(int attrIndex, String value) {
		List<List<String>> result = new ArrayList<List<String>>();
//		ArrayList<String> result = null;
//		for (int i = 0; i < datas.size(); i++) {
//			result = datas.get(i);
//			if (result.get(attrIndex).equals(value)) {
//				results.add(result);
//			}
//		}
		for (List<String> data : datas) {
			if (data.get(attrIndex).equals(value)) {
				result.add(data);
			}
		}
		return result;
	}
    
    public List<Object[]> getDatasArray() {
    	List<Object[]> result = new ArrayList<Object[]>();
    	for (List<String> data : datas) {
    		Object[] object = new Object[data.size()];
    		for (int i = 0, len = data.size(); i < len; i++) {
    			object[i] = data.get(i);
    		}
    		result.add(object);
    	}
    	return result;
    }
	
}
