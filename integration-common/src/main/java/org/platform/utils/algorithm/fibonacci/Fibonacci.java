package org.platform.utils.algorithm.fibonacci;

public class Fibonacci {

	private int fibonacci(int n) {
		if (n == 0 || n == 1) {
			return 1;
		}
		return fibonacci(n - 1) + fibonacci(n - 2);
	}
	
	private int fibonacci_iter(int n) {
		if (n == 0 || n == 1) {
			return 1;
		}
		int s1 = 1, s2 = 1, sum = 0;
		for (int i = 2; i <= n; i++) {
			sum = s1 + s2;
			s1 = s2;
			s2 = sum;
		}
		return sum;
	}
	
	public static void main(String[] args) {
		Fibonacci fibonacci = new Fibonacci();
		System.out.println(fibonacci.fibonacci(5));
		System.out.println(fibonacci.fibonacci_iter(5));
	}
}
