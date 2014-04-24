package org.platform.utils.algorithm.randomforest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DecisionCallable implements Callable<Object[]> {
	
	private String[] trainAttributes = null;
	private Object[][] trainDatas = null; 
	private Object[][] targetDatas = null;
	
	public DecisionCallable(Object[][] trainDatas, 
			String[] trainAttributes, Object[][] targetDatas) {
		this.trainDatas = trainDatas;
		this.trainAttributes = trainAttributes;
		this.targetDatas = targetDatas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] call() throws Exception {
		Object[] randomResult = TrainSetUtils.obtainRandomTrainSet(trainDatas, trainAttributes);
		String[] randomTrainAttributes = (String[]) randomResult[0];
		Map<Object, List<Sample>> randomSampleMap = (Map<Object, List<Sample>>) randomResult[1];
		SampleUtils.fill(randomSampleMap, randomTrainAttributes, 0);
		System.out.println(Thread.currentThread().getName() + " build decision tree start.");
		Object decisionTree = DecisionTree.buildWithC45(randomSampleMap, randomTrainAttributes);
		System.out.println(Thread.currentThread().getName() + " build decision tree stop.");
		return DecisionTree.decision(decisionTree, targetDatas, randomTrainAttributes);
	}
	
}
