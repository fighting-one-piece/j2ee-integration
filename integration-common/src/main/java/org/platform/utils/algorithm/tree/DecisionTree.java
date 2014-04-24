package org.platform.utils.algorithm.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTree {

	/** 计算熵 */
	public double calcShannoEntropy(List<Object[]> datas) {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
		for (Object[] data : datas) {
			Object key = data[data.length - 1];
			int value = map.containsKey(key) ? map.get(key) + 1 : 1;
			map.put(key, value);
		}
		double dataNum = datas.size();
		double shannoEntropy = 0.0;
		for (Map.Entry<Object, Integer> entry : map.entrySet()) {
			double p = entry.getValue() / dataNum;
			shannoEntropy -= p * (Math.log(p) / Math.log(2));
		}
		return shannoEntropy;
	}

	public double calcShannoEntropy(List<Object[]> datas, int column) {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
		for (Object[] data : datas) {
			Object key = data[column - 1];
			int value = map.containsKey(key) ? map.get(key) + 1 : 1;
			map.put(key, value);
		}
		double dataNum = datas.size();
		double shannoEntropy = 0.0;
		for (Map.Entry<Object, Integer> entry : map.entrySet()) {
			double p = entry.getValue() / dataNum;
			shannoEntropy -= p * (Math.log(p) / Math.log(2));
		}
		return shannoEntropy;
	}

	public List<Object[]> removeColumn(List<Object[]> datas, int targetCol) {
		List<Object[]> subDatas = new ArrayList<Object[]>();
		for (Object[] data : datas) {
			Object[] subData = new Object[data.length - 1];
			for (int i = 0, len = data.length; i < len; i++) {
				if (i != (targetCol - 1)) {
					subData[i] = data[i];
				}
			}
			subDatas.add(subData);
		}
		return subDatas;
	}

	public List<Object[]> removeLastColumn(List<Object[]> datas) {
		List<Object[]> subDatas = new ArrayList<Object[]>();
		for (Object[] data : datas) {
			Object[] subData = new Object[data.length - 1];
			for (int i = 0, len = data.length, last = len - 1; i < len; i++) {
				if (i != last) {
					subData[i] = data[i];
				}
			}
			subDatas.add(subData);
		}
		return subDatas;
	}

	public List<Object[]> splitDataSet(List<Object[]> datas, int targetCol,
			Object value) {
		List<Object[]> subDatas = new ArrayList<Object[]>();
		for (Object[] data : datas) {
			Object[] subData = new Object[data.length - 1];
			for (int i = 0, j = 0, len = data.length; i < len; i++) {
				if (i == (targetCol - 1)) {
					continue;
				}
				subData[j++] = data[i];
			}
			if (data[targetCol - 1].equals(value)) {
				subDatas.add(subData);
			}
		}
		return subDatas;
	}

	public Set<Object> getColumnValues(List<Object[]> datas, int column) {
		Set<Object> values = new HashSet<Object>();
		for (Object[] data : datas) {
			values.add(data[column]);
		}
		return values;
	}

	public int getBestFeature(List<Object[]> datas) {
		if (null == datas || datas.size() == 0) {
			return 0;
		}
		int bestFeature = 0;
		double bestGain = 0.0;
		double baseEntropy = calcShannoEntropy(datas);
		double dataNum = datas.size();
		int featureNum = datas.get(0).length - 1;
		for (int i = 0; i < featureNum; i++) {
			Object[] data = datas.get(i);
			double entropy = 0.0;
			for (Object d : data) {
				List<Object[]> subDatas = splitDataSet(datas, i + 1, d);
				double p = subDatas.size() / dataNum;
				entropy += p * calcShannoEntropy(subDatas);
			}
			double infoGain = baseEntropy - entropy;
			if (infoGain > bestGain) {
				bestGain = infoGain;
				bestFeature = i;
			}
		}
		return bestFeature;
	}

	public void genTree(List<Object[]> datas) {
		print(datas);
		Set<Object> classes = new HashSet<Object>();
		for (Object[] data : datas) {
			classes.add(data[data.length - 1]);
		}
		if (null == datas || datas.size() == 0)
			return;
		int bestFeature = getBestFeature(datas);
		System.out.println("bestFeature: " + bestFeature);
		Set<Object> columnValues = getColumnValues(datas, bestFeature);
		print(columnValues);
		if (columnValues.size() == 1)
			return;
		for (Object columnValue : columnValues) {
			List<Object[]> subDatas = splitDataSet(datas, bestFeature + 1,
					columnValue);
			genTree(subDatas);
		}
	}

	public void print(List<Object[]> datas) {
		for (Object[] data : datas) {
			System.out.print("[");
			for (Object d : data) {
				System.out.print(d + ",");
			}
			System.out.println("]");
		}
	}

	public void print(Set<Object> datas) {
		Iterator<Object> iterator = datas.iterator();
		System.out.print("set values: ");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + ",");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		List<Object[]> datas = new ArrayList<Object[]>();
		datas.add(new Object[] { 1, 1, "yes" });
		datas.add(new Object[] { 1, 1, "yes" });
		datas.add(new Object[] { 1, 0, "no" });
		datas.add(new Object[] { 0, 1, "no" });
		datas.add(new Object[] { 0, 1, "no" });
		DecisionTree decisionTree = new DecisionTree();
		// System.out.println(decisionTree.calcShannoEntropy(datas, 3));
		// decisionTree.print(decisionTree.splitDataSet(datas, 1, 1));
		// decisionTree.print(decisionTree.splitDataSet(datas, 1, 0));
		// System.out.println(decisionTree.getBestFeature(datas));
		// decisionTree.print(decisionTree.removeColumn(datas, 3));
		// decisionTree.print(decisionTree.removeLastColumn(datas));
		decisionTree.genTree(datas);
	}
}
