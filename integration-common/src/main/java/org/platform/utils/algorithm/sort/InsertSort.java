package org.platform.utils.algorithm.sort;

public class InsertSort extends AbstrSort<Integer> {

	@Override
	public void sort(Integer[] array) {
		int j;
	    for(int i = 1, len = array.length; i < len; i++){ 
	        int temp=array[i];
	        for(j = i; j>0 && temp < array[j - 1]; j--){  
	            array[j] = array[j-1];
	        }  
	        array[j]=temp;  
	    }  
		print(array);
	}
	
	public static void main(String[] args) {
		Integer[] array = new Integer[]{3,7,1,4,9,2,6,5,8};
		ISort<Integer> sort = new InsertSort();
		sort.sort(array);
	}
}
