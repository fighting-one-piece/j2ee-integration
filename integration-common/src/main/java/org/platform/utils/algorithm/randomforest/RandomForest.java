package org.platform.utils.algorithm.randomforest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.platform.utils.algorithm.ShowUtils;

public class RandomForest {
	
	public static Object[] build(Object[][] trainDatas, 
			String[] trainAttributes, Object[][] targetDatas, int random) {
		ExecutorService pools = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
		List<Future<Object[]>> futures = new ArrayList<Future<Object[]>>();
		for (int i = 0; i < random; i++) {
			DecisionCallable callable = new DecisionCallable(
					trainDatas, trainAttributes, targetDatas);
			futures.add(pools.submit(callable));
		}
		System.out.println("futures size: " + futures.size());
		System.out.println("trainAttributes len: " + trainAttributes.length);
		List<Object[]> results = new ArrayList<Object[]>();
		handleFuture(futures, results);
		int futureLen = futures.size();
		int resultsLen = results.size();
		while (resultsLen < futureLen) {
			handleFuture(futures, results);
			resultsLen = results.size();
		}
		return vote(results);
	}
	
	@SuppressWarnings("unchecked")
	public static Object[] buildNonConcurrent(Object[][] trainDatas, 
			String[] trainAttributes, Object[][] targetDatas, int random) {
		System.out.println("trainAttributes len: " + trainAttributes.length);
		List<Object[]> results = new ArrayList<Object[]>(); 
		String[] randomAttributes = null;
		Map<Object, List<Sample>> randomSamples = null;
		Object decisionTree = null;
		for (int i = 0; i < random; i++) {
			Object[] result = TrainSetUtils.obtainRandomTrainSet(trainDatas, trainAttributes);
			randomAttributes = (String[]) result[0];
			randomSamples = (Map<Object, List<Sample>>) result[1];
			decisionTree = DecisionTree.buildWithID3(randomSamples, randomAttributes);
			results.add(DecisionTree.decision(decisionTree, targetDatas, randomAttributes));
		}
		return vote(results);
	}
	
	private static void handleFuture(List<Future<Object[]>> futures, List<Object[]> results) {
		Iterator<Future<Object[]>> iterator = futures.iterator();
		while (iterator.hasNext()) {
			Future<Object[]> future = iterator.next();
			if (future.isDone()) {
				try {
					results.add(future.get());
					iterator.remove();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	private static Object[] vote(List<Object[]> results) {
		System.out.println("-----------results-------------");
		ShowUtils.print(results);
		System.out.println("-----------results-------------");
		int columnNum = results.get(0).length;
		Object[] finalResult = new Object[columnNum];
		for (int i = 0; i < columnNum; i++) {
			Map<Object, Integer> resultCount = new HashMap<Object, Integer>();
			for (Object[] result : results) {
				if (null == result[i]) continue;
				Integer count = resultCount.get(result[i]);
				resultCount.put(result[i], null == count ? 1 : count + 1);
			}
			int max = 0;
			Object maxResult = null;
			for (Map.Entry<Object, Integer> entry : resultCount.entrySet()) {
				if (max < entry.getValue()) {
					max = entry.getValue();
					maxResult = entry.getKey();
				}
			}
			finalResult[i] = maxResult;
		}
		return finalResult;
	}
	
}
