package org.platform.utils.algorithm.example;

public class Swap {
	
	private void swap_1(int a, int b) {
		int temp = a;
		a = b;
		b = temp;
		System.out.println(a + ":" + b);
	}
	
	private void swap_2(int a, int b) {
		a = a + b;
		b = a - b;
		a = a - b;
		System.out.println(a + ":" + b);
	}
	
	private void swap_3(int a, int b) {
		b = a ^ b;
		a = a ^ b;
		b = a ^ b;
		System.out.println(a + ":" + b);
	}
	
	public static void main(String[] args) {
		int a = 2, b = 3;
		Swap swap = new Swap();
		System.out.println(a + ":" + b);
		swap.swap_1(a, b);
		swap.swap_2(a, b);
		swap.swap_3(a, b);
	}
}
