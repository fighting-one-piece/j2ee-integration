package org.platform.utils.algorithm;

import java.util.List;

import org.junit.Test;
import org.platform.utils.algorithm.randomforest.RandomForest;
import org.platform.utils.algorithm.randomforest.Sample;
import org.platform.utils.algorithm.randomforest.SampleUtils;

public class RandomForestTest {
	
	private static String[] initialAttrNames = null;
	
	private static Object[][] initialDatas = null;
	
	static {
		initialAttrNames = new String[] { "AGE", "INCOME", "STUDENT", "CREDIT_RATING" };
		
		initialDatas = new Object[][] {
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
				{ ">40  ", "Medium", "No ", "Excellent", "0" }};
	}
	
	@Test
	public void test() {
		Object[][] datas = new Object[][] {
				{ "<30  ", "High  ", "No ", "Excellent", "0" },
				{ "30-40", "High  ", "No ", "Fair     ", "1" },
				{ ">40  ", "Medium", "No ", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Excellent", "0" },
				{ "<30  ", "High  ", "No ", "Excellent", "0" }};
		Object[] result = RandomForest.buildNonConcurrent(initialDatas, initialAttrNames, datas, 100);
		System.out.println("-----------------final result----------------");
		ShowUtils.print(result);
	}
	
	@Test
	public void test1() {
		Object[][] datas = new Object[][] {
				{ "<30  ", "High  ", "No ", "Excellent", "0" },
				{ "<30  ", "High  ", "No ", "Excellent", "0" },
				{ "30-40", "High  ", "No ", "Fair     ", "1" },
				{ ">40  ", "Medium", "No ", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Fair     ", "1" },
				{ ">40  ", "Low   ", "Yes", "Excellent", "0" }};
		Object[] result = RandomForest.build(initialDatas, initialAttrNames, datas, 1000);
		System.out.println("-----------------final result----------------");
		ShowUtils.print(result);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test2() {
		String trainFilePath = "d:\\trainset_extract_100.txt";
		Object[] datas = SampleUtils.readDatasFromFile(trainFilePath);
		String[] attributes = (String[]) datas[0];
		List<Sample> samples = (List<Sample>) datas[2];
		SampleUtils.fill(samples, attributes, 0);
		
		String targetFilePath = "d:\\trainset_extract_1.txt";
		Object[] targetDatas = SampleUtils.readDatasFromFile(targetFilePath);
		List<Sample> targetSamples = (List<Sample>) targetDatas[2];
		SampleUtils.fill(targetSamples, attributes, 0);
		
		Object[] result = RandomForest.build(SampleUtils.convert(samples, attributes), 
				attributes, SampleUtils.convert(targetSamples, attributes), 5);
		System.out.println("-----------------final result----------------");
		ShowUtils.print(result);
	}
}
