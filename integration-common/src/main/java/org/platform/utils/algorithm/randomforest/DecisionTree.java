package org.platform.utils.algorithm.randomforest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DecisionTree {
	
	/** ID3算法*/
	public static final int ID3 = 1;
	
	/** C4.5算法*/
	public static final int C45 = 2;
	
	/**
	 ** 基于ID3构造决策树
	 **/
	public static Object buildWithID3(Map<Object, List<Sample>> sampleMap, String[] attributes) {
		return build(sampleMap, attributes, ID3);
	}
	
	/**
	 ** 基于C4.5构造决策树
	 **/
	public static Object buildWithC45(Map<Object, List<Sample>> sampleMap, String[] attributes) {
		return build(sampleMap, attributes, C45);
	}
	
	/**
	 ** 构造决策树
	 *@param sampleMap 样本映射
	 *@param attributes 特征属性集
	 *@param algorithm 算法
	 **/
	@SuppressWarnings("unchecked")
	private static Object build(Map<Object, List<Sample>> sampleMap, String[] attributes, int algorithm) {
		// 如果只有一个样本，将该样本所属分类作为新样本的分类
		if (sampleMap.size() == 1) {
			return sampleMap.keySet().iterator().next();
		}
		// 如果没有供决策的属性，则将样本集中具有最多样本的分类作为新样本的分类，即投票选举出分类
		if (attributes.length == 0) {
			return obtainMaxCategory(sampleMap);
		}
		// 选取最优属性信息
		Object[] bestAttrInfo = null;
		if (algorithm == ID3) {
			bestAttrInfo = chooseBestAttributeWithID3(sampleMap, attributes);
		} else if (algorithm == C45) {
			bestAttrInfo = chooseBestAttributeWithC45(sampleMap, attributes);
		}
		// 决策树根结点，分支属性为选取的测试属性
		int bestAttrIndex = (Integer) bestAttrInfo[0];
		if (bestAttrIndex == -1) {
			return obtainMaxCategory(sampleMap);
		}
		Tree tree = new Tree(attributes[bestAttrIndex]);
		// 已用过的测试属性不应再次被选为测试属性
		String[] subA = new String[attributes.length - 1];
		for (int i = 0, j = 0; i < attributes.length; i++)
			if (i != (Integer) bestAttrInfo[0])
				subA[j++] = attributes[i];
		// 根据分支属性生成分支
		Map<Object, Map<Object, List<Sample>>> splits = 
				(Map<Object, Map<Object, List<Sample>>>) bestAttrInfo[2];
		for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
			Object attrValue = entry.getKey();
			Map<Object, List<Sample>> split = entry.getValue();
			Object child = build(split, subA, algorithm);
			tree.setChild(attrValue, child);
		}
		return tree;
	}

	/**
	 * ID3算法选取最优测试属性。最优是指如果根据选取的测试属性分支，则从各分支确定新样本
	 * 的分类需要的信息量之和最小，这等价于确定新样本的测试属性获得的信息增益最大
	 * 返回数组：选取的属性下标、信息量之和、Map(属性值->(分类->样本列表))
	 **/
	private static Object[] chooseBestAttributeWithID3(
			Map<Object, List<Sample>> sampleMap, String[] attributes) {
		int optIndex = -1; // 最优属性下标
		double minValue = Double.MAX_VALUE; // 最小信息量或说是期望
		Map<Object, Map<Object, List<Sample>>> optSplits = null; // 最优分支方案
		// 对每一个属性，计算将其作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和，选取最小为最优
		for (int attrIndex = 0; attrIndex < attributes.length; attrIndex++) {
			int allCount = 0; // 统计样本总数的计数器
			// 按当前属性构建Map：属性值->(分类->样本列表)
			Map<Object, Map<Object, List<Sample>>> curSplits = 
					new HashMap<Object, Map<Object, List<Sample>>>();
			for (Entry<Object, List<Sample>> entry : sampleMap.entrySet()) {
				Object category = entry.getKey();
				List<Sample> samples = entry.getValue();
				for (Sample sample : samples) {
					Object attrValue = sample.getAttribute(attributes[attrIndex]);
					Map<Object, List<Sample>> split = curSplits.get(attrValue);
					if (split == null) {
						split = new HashMap<Object, List<Sample>>();
						curSplits.put(attrValue, split);
					}
					List<Sample> splitSamples = split.get(category);
					if (splitSamples == null) {
						splitSamples = new LinkedList<Sample>();
						split.put(category, splitSamples);
					}
					splitSamples.add(sample);
				}
				allCount += samples.size();
			}
			// 计算将当前属性作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和
			double curValue = 0.0; // 计数器：累加各分支
			for (Map<Object, List<Sample>> splits : curSplits.values()) {
				double perSplitCount = 0;
				for (List<Sample> list : splits.values())
					perSplitCount += list.size();
				// 累计当前分支样本数
				double perSplitValue = 0.0; // 计数器：当前分支
				for (List<Sample> list : splits.values()) {
					double p = list.size() / perSplitCount;
					perSplitValue -= p * (Math.log(p) / Math.log(2));
				}
				curValue += (perSplitCount / allCount) * perSplitValue;
			}
			// 选取最小为最优
			if (minValue > curValue) {
				optIndex = attrIndex;
				minValue = curValue;
				optSplits = curSplits;
			}
		}
		return new Object[] {optIndex, minValue, optSplits};
	}
	
	/**
	 *	C4.5算法选取最优测试属性
	 *	选取最大增益率的为最优 
	 * 返回数组：选取的属性下标、增益率、Map(属性值->(分类->样本列表))
	 */
	private static Object[] chooseBestAttributeWithC45(
			Map<Object, List<Sample>> sampleMap, String[] attributes) {
		int optIndex = -1; // 最优属性下标
		double maxGainRatio = 0.0; // 最大增益率
		Map<Object, Map<Object, List<Sample>>> optSplits = null; // 最优分支方案
		// 对每一个属性，计算将其作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和，选取最优
		for (int attrIndex = 0; attrIndex < attributes.length; attrIndex++) {
			int allCount = 0; // 统计样本总数的计数器
			// 按当前属性构建Map：属性值->(分类->样本列表)
			Map<Object, Map<Object, List<Sample>>> curSplits = 
					new HashMap<Object, Map<Object, List<Sample>>>();
			for (Entry<Object, List<Sample>> entry : sampleMap.entrySet()) {
				Object category = entry.getKey();
				List<Sample> samples = entry.getValue();
				for (Sample sample : samples) {
					Object attrValue = sample.getAttribute(attributes[attrIndex]);
					Map<Object, List<Sample>> split = curSplits.get(attrValue);
					if (split == null) {
						split = new HashMap<Object, List<Sample>>();
						curSplits.put(attrValue, split);
					}
					List<Sample> splitSamples = split.get(category);
					if (splitSamples == null) {
						splitSamples = new LinkedList<Sample>();
						split.put(category, splitSamples);
					}
					splitSamples.add(sample);
				}
				allCount += samples.size();
			}
			// 计算将当前属性作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和
			double curValue = 0.0; // 计数器：累加各分支
			double splitInfo = 0.0; //分裂信息
			for (Map<Object, List<Sample>> splits : curSplits.values()) {
				double perSplitCount = 0;
				for (List<Sample> list : splits.values())
					perSplitCount += list.size();
				// 累计当前分支样本数
				double perSplitValue = 0.0; // 计数器：当前分支
				for (List<Sample> list : splits.values()) {
					double p = list.size() / perSplitCount;
					perSplitValue -= p * (Math.log(p) / Math.log(2));
				}
				double dj = (perSplitCount / allCount);
				curValue += dj * perSplitValue;
				splitInfo -= dj * (Math.log(dj) / Math.log(2));
			}
			double gainRatio = curValue / splitInfo;
			if (maxGainRatio <= gainRatio) {
				optIndex = attrIndex;
				maxGainRatio = gainRatio;
				optSplits = curSplits;
			}
		}
		return new Object[]{optIndex, maxGainRatio, optSplits};
	}
	
	/**
	 * 判定单条数据
	 * @return 数据类型
	 */
	public static Object decision (Object decisionTree, Object[] data, String[] attributes) {
		if (!(decisionTree instanceof Tree)) return decisionTree;
		Tree tree = (Tree) decisionTree;
		String attrName = tree.getAttribute();
		int index = attrNameIndex(attributes, attrName);
		if (index == -1) {
			return null;
		}
		Object value = data[index];
		for (Object attribute : tree.getAttributeValues()) {
			if (null == attribute || !attribute.equals(value)) {
				continue;
			}
			Object child = tree.getChild(attribute);
			if (child instanceof Tree) {
				return decision((Tree)child, data, attributes);
			} else {
				return child;
			}
		}
		return null;
	}
	
	/**
	 *	判定多条数据 
	 */
	public static Object[] decision (Object decisionTree, Object[][] datas, String[] attributes) {
		Object[] result = new Object[datas.length];
		for (int i = 0, len = datas.length; i < len; i++) {
			result[i] = decision(decisionTree, datas[i], attributes);
		}
		return result;
	}

	private static int attrNameIndex(String[] attributes, String attribute) {
		int index = -1;
		for (int i = 0, len = attributes.length; i < len; i++) {
			if (attribute.equals(attributes[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	private static Object obtainMaxCategory(Map<Object, List<Sample>> sampleMap) {
		int max = 0;
		Object maxCategory = null;
		for (Entry<Object, List<Sample>> entry : sampleMap.entrySet()) {
			int cur = entry.getValue().size();
			if (cur > max) {
				max = cur;
				maxCategory = entry.getKey();
			}
		}
		return maxCategory;
	}
	
	/** 
	 ** 将决策树输出到标准输出 
	 **/
	public static void outputDecisionTree(Object obj, int level, Object from) {
		for (int i = 0; i < level; i++)
			System.out.print("|-----");
		if (from != null)
			System.out.printf("(%s):", from);
		if (obj instanceof Tree) {
			Tree tree = (Tree) obj;
			String attrName = tree.getAttribute();
			System.out.printf("[%s = ?]\n", attrName);
			for (Object attrValue : tree.getAttributeValues()) {
				Object child = tree.getChild(attrValue);
				outputDecisionTree(child, level + 1, attrName + " = "
						+ attrValue);
			}
		} else {
			System.out.printf("[CATEGORY = %s]\n", obj);
		}
	}

}
