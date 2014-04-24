package org.platform.utils.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;

public class ShowUtils {
	
	private ShowUtils() {
		
	}
	
	public static <T> void print(T[] ts) {
		System.out.print("[");
		for (T t : ts) {
			System.out.print(t + ",");
		}
		System.out.println("]");
	}
	
	public static <T> void print(T[][] tss) {
		for (T[] ts : tss) {
			print(ts);
			System.out.println();
		}
	}
	
	public static <T> void print(Collection<T> collection) {
		for (T t : collection) {
			if (t instanceof Object[]) {
				print((Object[]) t);
			} else {
				System.out.println(t);
			}
		}
	}
	
	public static <K, V> void print(Map<K, V> map) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "------>" + entry.getValue());
		}
	}
	
	public static String format(double value) {
		BigDecimal decimal = BigDecimal.valueOf(value)
				.multiply(new BigDecimal(100), new MathContext(2, RoundingMode.HALF_UP));
		return String.valueOf(decimal.doubleValue());
	}
	
}
