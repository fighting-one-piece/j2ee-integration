package org.platform.utils.algorithm.tree;

public class BinaryTree {
	
	private void initData(TreeNode<String> treeNode) {
		TreeNode<String> a = new TreeNode<String>("A");
		TreeNode<String> b = new TreeNode<String>("B");
		treeNode.setLeftNode(a);
		treeNode.setRightNode(b);
		
		TreeNode<String> a1 = new TreeNode<String>("A1");
		TreeNode<String> a2 = new TreeNode<String>("A2");
		a.setLeftNode(a1);
		a.setRightNode(a2);
		
		TreeNode<String> b1 = new TreeNode<String>("B1");
		TreeNode<String> b2 = new TreeNode<String>("B2");
		b.setLeftNode(b1);
		b.setRightNode(b2);
		
		TreeNode<String> c1 = new TreeNode<String>("C1");
		TreeNode<String> c2 = new TreeNode<String>("C2");
		a1.setLeftNode(c1);
		a1.setRightNode(c2);
	}
	
	private void preSort(TreeNode<String> treeNode) {
		if (null == treeNode) return;
		System.out.print(" " + treeNode.getData());
		preSort(treeNode.getLeftNode());
		preSort(treeNode.getRightNode());
	}
	
	private void midSort(TreeNode<String> treeNode) {
		if (null == treeNode) return;
		midSort(treeNode.getLeftNode());
		System.out.print(" " + treeNode.getData());
		midSort(treeNode.getRightNode());
	}
	
	private void postSort(TreeNode<String> treeNode) {
		if (null == treeNode) return;
		postSort(treeNode.getLeftNode());
		postSort(treeNode.getRightNode());
		System.out.print(" " + treeNode.getData());
	}

	public static void main(String[] args) {
		BinaryTree binaryTree = new BinaryTree();
		
		TreeNode<String> treeNode = new TreeNode<String>("R");
		binaryTree.initData(treeNode);
		binaryTree.preSort(treeNode);
		System.out.println("------");
		binaryTree.midSort(treeNode);
		System.out.println("------");
		binaryTree.postSort(treeNode);
		System.out.println("------");
	}
}

class TreeNode<T> {
	
	private T data = null;
	private TreeNode<T> leftNode = null;
	private TreeNode<T> rightNode = null;
	
	public TreeNode() {
		
	}
	
	public TreeNode(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public TreeNode<T> getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(TreeNode<T> leftNode) {
		this.leftNode = leftNode;
	}
	public TreeNode<T> getRightNode() {
		return rightNode;
	}
	public void setRightNode(TreeNode<T> rightNode) {
		this.rightNode = rightNode;
	}
}