package org.platform.utils.algorithm.sort;

public class SelectSort extends AbstrSort<Integer> {
	
	@Override
	public void sort(Integer[] array) {
		for (int i = 0, len = array.length; i < len; i++) {
			int min = i;
			for (int j = i; j < len; j++) {
				if (array[min] > array[j]) {
					min = j;
				}
			}
			if (min != i) {
				swap(array, min, i);
			}
		}
		print(array);
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new SelectSort();
		sort.sort(array);
	}

}
