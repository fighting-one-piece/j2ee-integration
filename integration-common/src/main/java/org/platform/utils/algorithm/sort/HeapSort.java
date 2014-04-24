package org.platform.utils.algorithm.sort;

public class HeapSort extends AbstrSort<Integer> {

	@Override
	public void sort(Integer[] array) {
		buildHeap(array, array.length);
		print(array);
		System.out.println();
		System.out.println("------");
		for (int i = array.length - 1; i > 0; i--) {
			swap(array, 0, i);
			heapAdjust(array, 0, i);
			print(array);
			System.out.println();
		}
		System.out.println("------");
		print(array);
	}
	
	private void buildHeap(Integer[] array, int len) {
		for (int i = len / 2; i >= 0; i--) {
			heapAdjust(array, i, len);
		}
	}
	
	private void heapAdjust(Integer[] array, int i, int len) {
		int lchild = 2 * i + 1, rchild = 2 * i + 2;
		int max = i;
		if (i < len / 2) {
			if (lchild < len && array[lchild] > array[max]) max = lchild;
			if (rchild < len && array[rchild] > array[max]) max = rchild;
			if (max != i) {
				swap(array, max, i);
				heapAdjust(array, max, len);
			}
		}
		
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new HeapSort();
		sort.sort(array);
	}
}
