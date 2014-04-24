package org.platform.utils.algorithm.bayes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Example {
	
	private static int[] categories = null;
	
	private static List<String[]> data = new LinkedList<String[]>();
		
	private static int total_word_number = 0;
	
	private static Set<String> filter_words = new HashSet<String>();
	
	private static Set<String> filter_categories = null;
	
	static {
		data.add(new String[]{"my", "dog", "has", "flea", "problems", "help", "please"});
		data.add(new String[]{"maybe", "not", "take", "him", "to", "dog", "park", "stupid"});
		data.add(new String[]{"my", "dalmation", "is", "so", "cute", "I", "love", "him"});
		data.add(new String[]{"stop", "posting", "stupid", "worthless", "garbage"});
		data.add(new String[]{"mr", "licks", "ate", "my", "steak", "how", "to", "stop", "him"});
		data.add(new String[]{"quit", "buying", "worthless", "dog", "food", "stupid"});
		
		for (String[] array : data) {
			for (String word : array) {
				filter_words.add(word);
				total_word_number += 1;
			}
		}
		
		categories = new int[]{0, 1, 0, 1, 0, 1};
		System.out.println(categories);
	}
	
	public static int[] vec(String[] inputs) {
		int[] array = new int[filter_words.size()];
		for (String input : inputs) {
			int index = 0;
			for (String ds : filter_words) {
				if (input.equals(ds)) {
					array[index] = 1;
				}
				index++;
			}
		}
		return array;
	}
	
	
	public static void print(Map<Integer, String> map) {
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}
	}

	public static void main(String[] args) throws Exception {
		int[] array = Example.vec(data.get(1));
		for(int a : array) {
			System.out.print(a + ",");
		}
	}
}
 