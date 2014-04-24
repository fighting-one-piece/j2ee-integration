package org.platform.utils.algorithm.randomforest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

public class TrainSetUtils {
	
	private TrainSetUtils() {
		
	}
	
	public static void main(String[] args) {
		int row = 1;
		String tranSetPath = "d:\\trainset_extract.txt";
		extractTrainSet(tranSetPath, row);
	}
	
	/**
	 *	获取随机训练集 
	 *  第一个是随机特征属性、 第二个是随机样本类型映射
	 */
	public static Object[] obtainRandomTrainSet(Object[][] trainDatas, String[] trainAttributes) {
		Map<String, Integer> attribute2Column = new HashMap<String, Integer>();
		for (int i = 0, len = trainAttributes.length; i < len; i++) {
			attribute2Column.put(trainAttributes[i], i);
		}
		Random random = new Random();
		int minRandomAttributeCount = trainAttributes.length / 30;
		int randomAttributeCount = minRandomAttributeCount + 
				random.nextInt(trainAttributes.length - minRandomAttributeCount);
//		int randomAttributeCount = random.nextInt(trainAttributes.length);
//		while (randomAttributeCount <= minRandomAttributeCount) {
//			randomAttributeCount = random.nextInt(trainAttributes.length);
//		}
		Set<String> randomAttributeSet = new HashSet<String>();
		while (randomAttributeSet.size() != randomAttributeCount) {
			randomAttributeSet.add(trainAttributes[random.nextInt(trainAttributes.length)]);
		}
		String[] randomAttributes = randomAttributeSet.toArray(new String[0]);
		int minRandomDatasCount = trainDatas.length / 20;
		int randomDatasCount = minRandomDatasCount + 
				random.nextInt(trainDatas.length - minRandomDatasCount);
//		int randomDatasCount = random.nextInt(trainDatas.length);
//		while (randomDatasCount <= minRandomDatasCount) {
//			randomDatasCount = random.nextInt(trainDatas.length);
//		}
		System.out.println("randomAttributes length: " + randomAttributes.length);
		int randomDatasColumns = randomAttributes.length + 1;
		Object[][] randomDatas = new Object[randomDatasCount][randomDatasColumns];
		for (int i = 0; i < randomDatasCount; i++) {
			Object[] row = trainDatas[i];
			int j = 0;
			for (int len = randomAttributes.length; j < len; j++) {
				int index = attribute2Column.get(randomAttributes[j]);
				randomDatas[i][j] = row[index];
			}
			randomDatas[i][j] = row[row.length - 1];
		}
		return new Object[]{randomAttributes, SampleUtils.convert(randomDatas, randomAttributes)};
	}
	
	public static void extractTrainSet(String trainSetPath, int number) {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(trainSetPath))));
			String line = reader.readLine();
			Map<String, Integer> map = new HashMap<String, Integer>();
			while (!("").equals(line) && null != line) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				String category = tokenizer.nextToken();
				Integer value = map.get(category);
				value = null == value ? 1 : value + 1;
				map.put(category, value);
				if (value <= number) {
					System.out.println(line);
					lines.add(line);
				}
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
		int lastIndex = trainSetPath.lastIndexOf(".");
		trainSetPath.substring(0, lastIndex);
		trainSetPath.substring(lastIndex);
		StringBuilder file = new StringBuilder();
		file.append(trainSetPath.substring(0, lastIndex)).append("_")
			.append(number).append(trainSetPath.substring(lastIndex));
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(file.toString()))));
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		check(file.toString());
	}

	public static void check(String file) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(file))));
			String line = reader.readLine();
			while (!("").equals(line) && null != line) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				String category = tokenizer.nextToken();
				Integer value = map.get(category);
				value = null == value ? 1 : value + 1;
				map.put(category, value);
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
		for (Map.Entry<String, Integer> entry : map.entrySet() ) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
	
}
