package org.platform.utils.algorithm.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 增益*/
public class Gain {
	
	/** 计算熵*/
//	public static double calcShannoEntropy(List<Object[]> datas) {
//		Map<Object, Integer> map = new HashMap<Object, Integer>();
//		for (Object[] data : datas) {
//			Object key = data[data.length - 1];
//			int value = map.containsKey(key) ? map.get(key) + 1 : 1;
//			map.put(key, value);
//		}
//		double dataNum = datas.size();
//		double shannoEntropy = 0.0;
//		for (Map.Entry<Object, Integer> entry : map.entrySet()) {
//			double p = entry.getValue() / dataNum;
//			shannoEntropy -= p * (Math.log(p) / Math.log(2));
//		}
//		return shannoEntropy;
//	}
	
//	public static List<Object[]> splitDataSet(List<Object[]> datas, int targetCol, Object value) {
//		List<Object[]> subDatas = new ArrayList<Object[]>();
//		for (Object[] data : datas) {
//			Object[] subData = new Object[data.length - 1];
//			for (int i = 0, j = 0, len = data.length; i < len; i++) {
//				if (i == (targetCol - 1)) {
//					continue;
//				}
//				subData[j++] = data[i];
//			}
//			if (data[targetCol - 1].equals(value)) {
//				subDatas.add(subData);
//			}
//		}
//		return subDatas;
//	}
	
//	public static int getBestGainAttributeIndex(List<Object[]> datas) {
//		if (null == datas || datas.size() == 0) {
//			return 0;
//		}
//		int bestFeature = 0;
//		double bestGain = 0.0;
//		double baseEntropy = calcShannoEntropy(datas);
//		double dataNum = datas.size();
//		int featureNum = datas.get(0).length - 1;
//		for (int i = 0; i < featureNum; i++) {
//			Object[] data = datas.get(i);
//			double entropy = 0.0;
//			for (Object d : data) {
//				List<Object[]> subDatas = splitDataSet(datas, i + 1, d);
//				double p = subDatas.size() / dataNum;
//				entropy += p * calcShannoEntropy(subDatas);
//			}
//			double infoGain = baseEntropy - entropy;
//			if (infoGain > bestGain) {
//				bestGain = infoGain;
//				bestFeature = i;
//			}
//		}
//		return bestFeature;
//	}
	
	/** 计算熵*/
	public static double calcShannoEntropy(List<List<String>> datas) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (List<String> data : datas) {
			String key = data.get(data.size() - 1);
			int value = map.containsKey(key) ? map.get(key) + 1 : 1;
			map.put(key, value);
		}
		double dataNum = datas.size();
		double shannoEntropy = 0.0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			double p = entry.getValue() / dataNum;
			shannoEntropy -= p * (Math.log(p) / Math.log(2));
		}
		return shannoEntropy;
	}
	
	public static List<List<String>> splitDataSet(List<List<String>> datas, int targetCol, Object value) {
		List<List<String>> subDatas = new ArrayList<List<String>>();
		for (List<String> data : datas) {
			List<String> subData = new ArrayList<String>();
			subData.addAll(data);
			for (int i = 0, len = data.size(); i < len; i++) {
				if (i == (targetCol - 1)) {
					subData.remove(i);
				}
			}
			if (data.get(targetCol - 1).equals(value)) {
				subDatas.add(subData);
			}
		}
		return subDatas;
	}
	
	public static int getBestGainAttributeIndex(List<List<String>> datas) {
		if (null == datas || datas.size() == 0) {
			return 0;
		}
		int bestAttributeIndex = 0;
		double bestGain = 0.0;
		double baseEntropy = calcShannoEntropy(datas);
		double dataNum = datas.size();
		int featureNum = datas.get(0).size() - 1;
		for (int i = 0; i < featureNum; i++) {
			List<String> data = datas.get(i);
			double entropy = 0.0;
			for (String d : data) {
				List<List<String>> subDatas = splitDataSet(datas, i + 1, d);
				double p = subDatas.size() / dataNum;
				entropy += p * calcShannoEntropy(subDatas);
			}
			double infoGain = baseEntropy - entropy;
			if (infoGain > bestGain) {
				bestGain = infoGain;
				bestAttributeIndex = i;
			}
		}
		return bestAttributeIndex;
	}
	
}
