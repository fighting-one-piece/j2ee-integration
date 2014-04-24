package org.platform.utils.algorithm.sort;

public class QuickSort extends AbstrSort<Integer> {
	
	@Override
	public void sort(Integer[] array) {
		quickSort(array, 0, array.length - 1);
		print(array);
	}
	
	private void quickSort(Integer[] array, int low, int high) {
		if (low > high) return;
		int partition = partition(array, low, high);
		quickSort(array, low, partition - 1);
		quickSort(array, partition + 1, high);
	}
	
	private int partition(Integer[] array, int low, int high) {
		int partition = array[low];
		while (low < high) {
			while (low < high && array[high] >= partition) {
				high--;
			}
			array[low] = array[high];
			while (low < high && array[low] <= partition) {
				low++;
			}
			array[high] = array[low];
		}
		array[low] = partition;
		return low;
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new QuickSort();
		sort.sort(array);
	}
}
