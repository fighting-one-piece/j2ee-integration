package org.platform.utils.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.platform.utils.algorithm.randomforest.DecisionTree;
import org.platform.utils.algorithm.randomforest.Sample;
import org.platform.utils.algorithm.randomforest.SampleUtils;

public class DecisionTreeTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test5() {
		String filePath = "d:\\trainset_100.txt";
		Object[] datas = SampleUtils.readDatasFromFile(filePath);
		Map<Object, List<Sample>> sampleMap = (Map<Object, List<Sample>>) datas[1];
		Set<String> attributeSet = new HashSet<String>();
		Map<String, Integer> attributeMap = (Map<String, Integer>) datas[3];
		for (Map.Entry<String, Integer> entry : attributeMap.entrySet()) {
			String attribute = entry.getKey();
			int count = entry.getValue();
			if (count == 1) continue;
			attributeSet.add(attribute);
		}
		String[] attributes = attributeSet.toArray(new String[0]);
		Object decisionTree = DecisionTree.buildWithID3(sampleMap, attributes);
		
		String targetPath = "d:\\trainset_1.txt";
		Object[] target = SampleUtils.readDatasFromFile(targetPath);
		Object[][] targetDatas = SampleUtils.convert((List<Sample>) target[2], attributes);
		Object[] result = DecisionTree.decision(decisionTree, targetDatas, attributes);
		ShowUtils.print((Map<Object, List<Sample>>) target[1]);
		ShowUtils.print(result);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test4() {
		String filePath = "d:\\trainset_1000.txt";
		Object[] datas = SampleUtils.readDatasFromFile(filePath);
		Map<String, Integer> attributeMap = (Map<String, Integer>) datas[3];
		double attribute_all_count = attributeMap.values().size();
		System.out.println("all count: " + attribute_all_count);
		int i = 0;
		for (Map.Entry<String, Integer> entry : attributeMap.entrySet()) {
			String attribute = entry.getKey();
			int count = entry.getValue();
			if (count < 50) continue;
			double p = count / attribute_all_count;
			System.out.println(attribute + " : " + count + " : " + convert(p));
			i++;
		}
		System.out.println(i);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test3() {
		String filePath = "d:\\trainset_extract_100.txt";
		Object[] datas = SampleUtils.readDatasFromFile(filePath);
		String[] attributes = (String[]) datas[0];
		Map<Object, List<Sample>> sampleMap = (Map<Object, List<Sample>>) datas[1];
		SampleUtils.fill(sampleMap, attributes, 0);
//		ShowUtils.print(sampleMap);
		Object decisionTree = DecisionTree.buildWithID3(sampleMap, attributes);
//		DecisionTree.outputDecisionTree(decisionTree, 0, null);
		
		String targetPath = "d:\\trainset_extract_1.txt";
		Object[] target = SampleUtils.readDatasFromFile(targetPath);
		Object[][] targetDatas = SampleUtils.convert((List<Sample>) target[2], attributes);
		Object[] result = DecisionTree.decision(decisionTree, targetDatas, attributes);
		ShowUtils.print((Map<Object, List<Sample>>) target[1]);
		ShowUtils.print(result);
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test2() {
		String filePath = "d:\\trainset_10.txt";
		Object[] datas = SampleUtils.readDatasFromFile(filePath);
		String[] attributes = (String[]) datas[0];
		System.out.println("attributes length: " + attributes.length);
		List<Sample> samples = (List<Sample>) datas[2];
		Object[][] resultss = new Object[samples.size()][attributes.length];
		for (int i = 0, len = samples.size(); i < len; i++) {
			Sample sample = samples.get(i);
			resultss[i][attributes.length - 1] = sample.getCategory();
			Map<String, Object> sampleAttrs = sample.getAttributes();
			Object attrValue = null;
			for (int j = 0, attrLen = attributes.length; j < attrLen; j++) {
				attrValue = sampleAttrs.get(attributes[j]);
				resultss[i][j]  = null == attrValue ? 0 : attrValue;
			}
		}
		ShowUtils.print(resultss);
		Object decisionTree = DecisionTree.buildWithID3(SampleUtils.convert(resultss, attributes), attributes);
		DecisionTree.outputDecisionTree(decisionTree, 0, null);
	}

	@Test
	public void test1() throws Exception {
		String[] attributes = new String[] { "AGE", "INCOME", "STUDENT", "CREDIT_RATING" };
		Map<Object, List<Sample>> samples = readSampleMap(attributes);
		Object decisionTree = DecisionTree.buildWithID3(samples, attributes);
		DecisionTree.outputDecisionTree(decisionTree, 0, null);
		Object[] data = new Object[] { ">40  ", "Low   ", "Yes", "Excellent","0" };
		System.out.println(DecisionTree.decision(decisionTree, data, attributes));
		Object[][] datas = new Object[][] {
				{ "30-40", "High  ", "No ", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Excellent", "0" } };
		Object[] result = DecisionTree.decision(decisionTree, datas, attributes);
		System.out.println(result[0]);
		System.out.println(result[1]);
	}
	
	@Test
	public void test0() throws Exception {
		Object[][] datas = new Object[][] {
				{ "30-40", "High  ", "No ", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Excellent", "0" } };
		String[] attributes = new String[] { "AGE", "INCOME", "STUDENT", "CREDIT_RATING" };
		Map<Object, List<Sample>> samples = readSampleMap(attributes);
		Object decisionTreeID3 = DecisionTree.buildWithID3(samples, attributes);
		DecisionTree.outputDecisionTree(decisionTreeID3, 0, null);
		ShowUtils.print(DecisionTree.decision(decisionTreeID3, datas, attributes));
		Object decisionTreeC45 = DecisionTree.buildWithC45(samples, attributes);
		DecisionTree.outputDecisionTree(decisionTreeC45, 0, null);
		ShowUtils.print(DecisionTree.decision(decisionTreeC45, datas, attributes));
	}

	/**
	 ** 读取已分类的样本集，返回Map：分类 -> 属于该分类的样本的列表
	 **/
	private Map<Object, List<Sample>> readSampleMap(String[] attributes) {
		// 样本属性及其所属分类（数组中的最后一个元素为样本所属分类）
		Object[][] datas = new Object[][] {
				{ "<30  ", "High  ", "No ", "Fair     ", "0" },
				{ "<30  ", "High  ", "No ", "Excellent", "0" },
				{ "30-40", "High  ", "No ", "Fair     ", "1" },
				{ ">40  ", "Medium", "No ", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Excellent", "0" },
				{ "30-40", "Low   ", "Yes", "Excellent", "1" },
				{ "<30  ", "Medium", "No ", "Fair     ", "0" },
				{ "<30  ", "Low   ", "Yes", "Fair     ", "1" },
				{ ">40  ", "Medium", "Yes", "Fair     ", "1" },
				{ "<30  ", "Medium", "Yes", "Excellent", "1" },
				{ "30-40", "Medium", "No ", "Excellent", "1" },
				{ "30-40", "High  ", "Yes", "Fair     ", "1" },
				{ ">40  ", "Medium", "No ", "Excellent", "0" } };
		return SampleUtils.convert(datas, attributes);
	}
	
	private double convert(double value) {
		return BigDecimal.valueOf(value).multiply(new BigDecimal(100),
				new MathContext(2, RoundingMode.HALF_UP)).doubleValue();
	}
	
}
