package org.platform.utils.algorithm.sort;

public class BubbleSort extends AbstrSort<Integer> {
	
	@Override
	public void sort(Integer[] array) {
		for (int i = 0, len = array.length; i < len - 1; i++) {
			for (int j = 0; j < len - i - 1; j++) {
				if (array[j] > array[j + 1]) {
					swap(array, j, j + 1);
				}
			}
		}
		print(array);
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new BubbleSort();
		sort.sort(array);
	}

}
