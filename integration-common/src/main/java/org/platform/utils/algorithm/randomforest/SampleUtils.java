package org.platform.utils.algorithm.randomforest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class SampleUtils {

	private SampleUtils() {
		
	}
	
	/**
	 *	转换为分类的样本集，返回Map：分类 -> 属于该分类的样本的列表
	 **/
	public static Map<Object, List<Sample>> convert(Object[][] datas, String[] attributes) {
		// 读取样本属性及其所属分类，构造表示样本的Sample对象，并按分类划分样本集
		Map<Object, List<Sample>> result = new HashMap<Object, List<Sample>>();
		for (Object[] data : datas) {
			Sample sample = new Sample();
			int i = 0;
			for (int n = data.length - 1; i < n; i++)
				sample.setAttribute(attributes[i], data[i]);
			sample.setCategory(data[i]);
			List<Sample> samples = result.get(data[i]);
			if (samples == null) {
				samples = new LinkedList<Sample>();
				result.put(data[i], samples);
			}
			samples.add(sample);
		}
		return result;
	}
	
	/**
	 *	转换为数组的样本集，返回[][]
	 **/
	public static Object[][] convert(List<Sample> samples, String[] attributes) {
		Object[][] resultss = new Object[samples.size()][attributes.length + 1];
		for (int i = 0, len = samples.size(); i < len; i++) {
			Sample sample = samples.get(i);
			resultss[i][attributes.length] = sample.getCategory();
			Map<String, Object> sampleAttrs = sample.getAttributes();
			Object attrValue = null;
			for (int j = 0, attrLen = attributes.length; j < attrLen; j++) {
				attrValue = sampleAttrs.get(attributes[j]);
				resultss[i][j]  = null == attrValue ? 0 : attrValue;
			}
		}
		return resultss;
	}
	
	public static void fill(Map<Object, List<Sample>> sampleMap, 
			String[] attributes, Object fillValue) {
		for (List<Sample> samples : sampleMap.values()) {
			fill(samples, attributes, fillValue);
		}
	}
	
	public static void fill(List<Sample> samples, String[] attributes, Object fillValue) {
		fillValue = null == fillValue ? 0 : fillValue;
		for (Sample sample : samples) {
			Map<String, Object> sampleAttrs = sample.getAttributes();
			Object attrValue = null;
			for (int i = 0, attrLen = attributes.length; i < attrLen; i++) {
				attrValue = sampleAttrs.get(attributes[i]);
				sampleAttrs.put(attributes[i], 
						null == attrValue ? fillValue : attrValue);
			}
		}
	}
	
	/**
	 *	从文件中读取数据信息
	 *  第一个是所有特征属性、第二个是类型样本映射、第三个是所有样本列表、第四个为特征属性统计映射
	 */
	public static Object[] readDatasFromFile(String filePath) {
		Set<String> attributes = new HashSet<String>();
		Map<String, Integer> attributeMap = new HashMap<String, Integer>();
		Map<Object, List<Sample>> sampleMap = new HashMap<Object, List<Sample>>();
		List<Sample> sampleList = new ArrayList<Sample>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filePath))));
			Sample sample = null;
			String line = reader.readLine();
			while (!("").equals(line) && null != line) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				sample = new Sample();
				sample.setCategory(tokenizer.nextToken());
				while (tokenizer.hasMoreTokens()) {
					String value = tokenizer.nextToken();
					String[] entry = value.split(":");
					sample.setAttribute(entry[0], entry[1]);
					if (!attributes.contains(entry[0])) {
						attributes.add(entry[0]);
					}
					Integer attributeCount = attributeMap.get(entry[0]);
					attributeMap.put(entry[0], null == attributeCount ? 
							1 : attributeCount + 1);
				}
				List<Sample> samples = sampleMap.get(sample.getCategory());
				if (null == samples) {
					samples = new ArrayList<Sample>();
					sampleMap.put(sample.getCategory(), samples);
				}
				samples.add(sample);
				sampleList.add(sample);
				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new Object[] {attributes.toArray(new String[0]), 
				sampleMap, sampleList, attributeMap};
	}
	
}
