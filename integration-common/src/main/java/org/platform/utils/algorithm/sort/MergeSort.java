package org.platform.utils.algorithm.sort;

public class MergeSort extends AbstrSort<Integer> { 
	
	private Integer[] temp = null;

	@Override
	public void sort(Integer[] array) {
		temp = new Integer[array.length];
		doSort(array, 0, array.length - 1);
		print(array);
	}
	
	private void doSort(Integer[] array, int low, int high) {
		if (low >= high) return;
		int mid = (low + high) / 2;
		doSort(array, low, mid);
		doSort(array, mid + 1, high);
		doMerge(array, low, mid, high);
	}
	
	private void doMerge(Integer[] array, int low, int mid, int high) {
		int i = low, j = mid + 1;
		for (int k = low; k <= high; k++) {
			temp[k] = array[k];
		}
		for (int k = low; k <= high; k++) {
			if (i > mid) {  
                array[k] = temp[j++];  
            } else if (j > high) {  
                array[k] = temp[i++];  
            } else if (temp[i] < temp[j]) {
				array[k] = temp[i++];
			} else {
				array[k] = temp[j++];
			}
		}
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new MergeSort();
		sort.sort(array);
	}
}
