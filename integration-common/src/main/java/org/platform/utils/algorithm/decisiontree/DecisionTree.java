package org.platform.utils.algorithm.decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class DecisionTree {

	public void print(Set<String> datas) {
		Iterator<String> iterator = datas.iterator();
		System.out.print("set values: ");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + ",");
		}
		System.out.println();
	}

	public void print(List<String> datas) {
		Iterator<String> iterator = datas.iterator();
		System.out.print("list values: ");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + ",");
		}
		System.out.println();
	}
	
	public void printList(List<List<String>> datass) {
		for (List<String> datas : datass) {
			print(datas);
		}
	}
	
	/**
	 * 递归打印树结构
	 * @param root 当前待输出信息的结点
	 */
	public void print(TreeNode root) {
		System.out.println("name:" + root.getName());
		Set<String> categories = root.getCategories();
		System.out.print("node categories: {");
		for (String category : categories) {
			System.out.print(category + " ");
		}
		System.out.print("}");
		System.out.println("");
		Set<TreeNode> children = root.getChildren();
		int size = children.size();
		if (size == 0) {
			System.out.println("-->leaf node<--");
		} else {
			System.out.println("size of children:" + children.size());
			int i = 0;
			for (TreeNode child : children) {
				System.out.print("child " + (++i) + " of node " + root.getName() + " : " + child.getName());
//				print(child);
			}
		}
	}


	/**
	 * 构造决策树
	 * @param datas 训练元组集合
	 * @param candidateAttributes 候选属性集合
	 * @return 决策树根结点
	 */
	public TreeNode buildTree(List<List<String>> datas, List<String> candidateAttributes) {
		TreeNode node = new TreeNode();
		node.setDatas(datas);
		node.setCandidateAttributes(candidateAttributes);
		Map<String, Integer> classes = node.getDatasClassificationCountMap();
		String maxC = node.getDatasMaxClassification(classes);
		System.out.println("maxC: " + maxC);
		if (classes.size() == 1 || candidateAttributes.size() == 0) {
			node.setName(maxC);
			return node;
		}
		int bestAttrIndex = Gain.getBestGainAttributeIndex(datas);
		System.out.println("bestAttrIndex: " + bestAttrIndex);
		Set<String> categories = node.getDatasCategoriesWithIndex(bestAttrIndex);
		print(categories);
		node.setCategories(categories);
		print(candidateAttributes);
		node.setName(candidateAttributes.get(bestAttrIndex));
//		if (categories.size() > 2) { // ?此处有待商榷
//			candidateAttributes.remove(bestAttrIndex);
//			print(candidateAttributes);
//		}
		List<String> subCandidateAttributes = new ArrayList<String>();
		for (int i = 0, len = candidateAttributes.size(); i < len; i++ ) {
			if (i != bestAttrIndex) {
				subCandidateAttributes.add(candidateAttributes.get(i));
			}
		}
		for (String category : categories) {
			List<List<String>> subDatas = node.getDatasWithIndexAndValue(
					bestAttrIndex, category);
			for (int i = 0; i < subDatas.size(); i++) {
				subDatas.get(i).remove(bestAttrIndex);
			}
			System.out.println(category + ":");
			printList(subDatas);
//			if (subDatas.size() == 0) {
//				TreeNode leafNode = new TreeNode();
//				leafNode.setName(maxC);
//				leafNode.setDatas(subDatas);
//				leafNode.setCandidateAttributes(subCandidateAttributes);
//				System.out.println("node: " + node.getName() + " ----> leafNode: "
//						+ leafNode.getName());
//				node.getChildren().add(leafNode);
//			} else {
				TreeNode newNode = buildTree(subDatas, subCandidateAttributes);
				System.out.println("node: " + node.getName() + " ----> newNode: "
						+ newNode.getName());
				node.getChildren().add(newNode);
//			}
		}
		return node;
	}


	/**
	 * 主函数，程序入口
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ArrayList<String> candidateAttributes = new ArrayList<String>();
		candidateAttributes.add("age");
		candidateAttributes.add("income");
		candidateAttributes.add("student");
		candidateAttributes.add("credit_rating");
		List<List<String>> datas = new ArrayList<List<String>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File("d:\\trans.txt"))));
		String line = reader.readLine();
		while (!("").equals(line) && null != line) {
			StringTokenizer tokenizer = new StringTokenizer(line);
			ArrayList<String> parts = new ArrayList<String>();
			while (tokenizer.hasMoreTokens()) {
				parts.add(tokenizer.nextToken());
			}
			datas.add(parts);
			line = reader.readLine();
		}
		DecisionTree tree = new DecisionTree();
		TreeNode root = tree.buildTree(datas, candidateAttributes);
		System.out.println("---------------------------------------");
		tree.print(root);
	}
}
