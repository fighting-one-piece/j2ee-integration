package org.platform.utils.algorithm.sort;

public class AbstrSort<T> implements ISort<T> {

	@Override
	public void sort(T[] array) {
	}
	
	protected void swap(T[] array, int left, int right) {
		T temp = array[left];
		array[left] = array[right];
		array[right] = temp;
	}
	
	protected void print(T[] array) {
		for (T t : array) {
			System.out.print(t + " ");
		}
	}
	
}
