package org.platform.utils.algorithm.example;

public class SubString {
	
	private void seriesSubString(char[] characters) {
		for (int i = 0, len = characters.length; i < len; i++) {
			for (int j = i; j < len; j++ ) {
				for (int k = i; k <= j; k++) {
					System.out.print(characters[k]);
					System.out.print("\t");
				}
				System.out.println();
			}
		}
	}
	
	private int maxSeriesSubStringNum(int[] arrays) {
		int maxNum = 1;
		for (int i = 0, len = arrays.length; i < len - 1; i++) {
			if ((arrays[i] + 1) == arrays[i + 1]) {
				maxNum += 1;
			} else {
				maxNum = 1;
			}
		}
		return maxNum;
	}
	
	private void leftShiftOne(char[] characters) {
		char temp = characters[0];
		for (int i = 1, len = characters.length; i < len; i++) {
			characters[i - 1] = characters[i];
		}
		characters[characters.length - 1] = temp;
	}
	
	private void leftShift(char[] characters, int leftNum) {
		while (leftNum > 0) {
			leftShiftOne(characters);
			leftNum--;
		}
	}
	
	private static void print(char[] characters) {
		for (char character : characters) {
			System.out.println(character);
		}
	}

	public static void main(String[] args) {
		String string = "abcde";
		char[] characters = string.toCharArray();
		SubString subString = new SubString();
		subString.seriesSubString(characters);
		int[] arrays = new int[]{1, 2, 5 , 6 , 2, 3, 4, 9, 2, 1, 3, 4, 5};
		System.out.println(subString.maxSeriesSubStringNum(arrays));
		subString.leftShift(characters, 2);
		print(characters);
	}
}
