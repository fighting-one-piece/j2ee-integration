package org.platform.utils.algorithm;

import java.util.Map;

public class AlgorithmUtils {
	
	/** 计算平均值*/
	public static double average(double[] values) {
		double sum = 0, length = values.length;
		for (int i = 0; i < length; i++){
			sum += values[i];
		}
		return sum / length;
	}
	
	public static <T> double average(T[] values) {
		double sum = 0;
		
		return sum;
	}
	
	/** 计算方差*/
	public static double variance(double[] values) {
		double sum = 0, length = values.length;
		double average = average(values);
		for (int i = 0; i < length; i++) {
			sum += (values[i] - average) * (values[i] - average);
		}
		return sum / length;
	}
	
	/** 计算样本方差*/
	public static double example_variance(double[] values) {
		double sum = 0, length = values.length;
		double average = average(values);
		for (int i = 0; i < length; i++) {
			sum += (values[i] - average) * (values[i] - average);
		}
		return sum / (length - 1);
	}
	
	/** 计算期望*/
	public static double except(Map<Double, Double> values) {
		double sum = 0;
		for (Map.Entry<Double, Double> entry : values.entrySet()) {
			sum += entry.getKey() * entry.getValue();
		}
		return sum;
	}

	public static void main(String[] args) {
		double[] values = new double[]{6, 5.92, 5.58, 5.92, 5, 5.5, 5.42, 5.75};
		System.out.println(AlgorithmUtils.average(values));
		double[] values1 = new double[]{6, 5.92, 5.58, 5.92};
		System.out.println(AlgorithmUtils.average(values1));
		System.out.println(AlgorithmUtils.variance(values1));
	}
}
