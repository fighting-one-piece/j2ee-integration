package org.platform.utils.binary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinaryUtils {

	/**
	 * 将二进制整数部分转换成十进制
	 * 
	 * @param inteter
	 *            二进制整数部分字符串
	 * @return 转换后的十进制数值
	 */
	public static int binaryIntToDecimalism(String inteter) {
		int inteterSum = 0;
		for (int i = inteter.length(); i > 0; i--) {
			int scale = 2;
			if (inteter.charAt(-(i - inteter.length())) == '1') {
				if (i != 1) {
					for (int j = 1; j < i - 1; j++) {
						scale *= 2;
					}
				} else {
					scale = 1;
				}
			} else {
				scale = 0;
			}
			inteterSum += scale;
		}
		return inteterSum;
	}

	/**
	 * 将二进制小数部分转换成十进制
	 * 
	 * @param decimals
	 *            二进制小数部分字符串
	 * @return 转换后的十进制数值
	 */
	public static double binaryDecToDecimalism(String decimals) {
		double decimalsSum = 0f;
		for (int i = 0; i < decimals.length(); i++) {
			double scale = 2;
			if (decimals.charAt(i) == '1') {
				if (i == 0) {
					scale = 1 / scale;
				} else {
					for (int j = 1; j <= i; j++) {
						scale *= 2;
					}
					scale = 1 / scale;
				}
			} else {
				scale = 0;
			}
			decimalsSum += scale;
		}
		return decimalsSum;
	}

	/**
	 * 将二进制转换成十进制
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return 转换后的十进制字符串
	 */
	public static String binaryToDecimalism(String binary) {
		String sum = "";
		String integer = ""; // 整数部分
		String decimals = ""; // 小数部分
		int integerSum = 0; // 整数部分和
		double decimalsSum = 0d; // 小数部分和
		if (BinaryUtils.isBinary(binary)) {
			if (BinaryUtils.isContainsPoint(binary)) {
				integer = binary.substring(0, binary.indexOf("."));
				decimals = binary.substring(binary.indexOf(".") + 1,
						binary.length());
				integerSum = BinaryUtils.binaryIntToDecimalism(integer);
				decimalsSum = BinaryUtils.binaryDecToDecimalism(decimals);
				sum = String.valueOf(integerSum + decimalsSum);
			} else {
				integerSum = BinaryUtils.binaryIntToDecimalism(binary);
				sum = String.valueOf(integerSum);
			}
		} else {
			try {
				throw new IllegalBinaryException(binary + " Illegal binary!");
			} catch (IllegalBinaryException be) {
				System.out.println(be.getMessage());
			}
		}
		return sum;
	}

	/**
	 * 将二进制整数部分转换成八进制
	 * 
	 * @param integer
	 *            二进制字符串
	 * @return 转换后的八进制字符串
	 */
	public static String binaryIntToOctal(String integer) {
		StringBuilder integerSum = new StringBuilder();
		int loop = 0; // 循环次数
		if (integer.length() % 3 == 0) {
			loop = integer.length() / 3;
		} else {
			loop = integer.length() / 3 + 1;
		}
		String binary = "";
		for (int i = 1; i <= loop; i++) {
			if (i != loop) {
				binary = integer.substring(integer.length() - i * 3,
						integer.length() - i * 3 + 3);
			} else {
				binary = BinaryUtils.appendZero(
						integer.substring(0, integer.length() - (i - 1) * 3),
						3, true);
			}
			integerSum.append(BinaryUtils.binaryIntToDecimalism(binary));
		}
		return integerSum.reverse().toString();
	}

	/**
	 * 将二进制小数部分转换成八进制
	 * 
	 * @param xs
	 *            二进制字符串
	 * @return 转换后的八进制字符串
	 */
	public static String binaryDecToOctal(String decimals) {
		StringBuilder decimalsSum = new StringBuilder();
		int loop = 0; // 循环次数
		if (decimals.length() % 3 == 0) {
			loop = decimals.length() / 3;
		} else {
			loop = decimals.length() / 3 + 1;
		}
		String binary = "";
		for (int i = 1; i <= loop; i++) {
			if (i != loop) {
				binary = decimals.substring(3 * (i - 1), 3 * (i - 1) + 3);
			} else {
				binary = BinaryUtils.appendZero(decimals.substring(3 * (i - 1)),
						3, false);
			}
			decimalsSum.append(BinaryUtils.binaryIntToDecimalism(binary));
		}
		return decimalsSum.toString();
	}

	/**
	 * 将二进制转换成八进制
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return 转换后的八进制字符串
	 */
	public static String binaryToOctal(String binary) {
		String integer = "";
		String point = "";
		String decimals = "";
		String integerSum = "";
		String decimalsSum = "";
		if (BinaryUtils.isBinary(binary)) {
			if (BinaryUtils.isContainsPoint(binary)) {
				integer = binary.substring(0, binary.indexOf("."));
				point = ".";
				decimals = binary.substring(binary.indexOf(".") + 1,
						binary.length());
				integerSum = BinaryUtils.binaryIntToOctal(integer);
				decimalsSum = BinaryUtils.binaryDecToOctal(decimals);
			} else {
				integerSum = BinaryUtils.binaryIntToOctal(binary);
			}
		} else {
			try {
				throw new IllegalBinaryException(binary + " Illegal binary!");
			} catch (IllegalBinaryException be) {
				System.out.println(be.getMessage());
			}
		}
		StringBuilder sum = new StringBuilder();
		sum = sum.append(integerSum).append(point).append(decimalsSum);
		return sum.toString();
	}

	/**
	 * 将二进制整数部分转换成十六进制
	 * 
	 * @param integer
	 *            二进制整数部分字符串
	 * @return 转换后的十六进制字符串
	 */
	public static String binaryIntToHexadecimal(String integer) {
		StringBuffer integerSum = new StringBuffer();
		int loop = 0; // 循环次数
		if (integer.length() % 4 == 0) {
			loop = integer.length() / 4;
		} else {
			loop = integer.length() / 4 + 1;
		}
		String binary = "";
		for (int i = 1; i <= loop; i++) {
			if (i != loop) {
				binary = integer.substring(integer.length() - i * 4,
						integer.length() - i * 4 + 4);
			} else {
				binary = BinaryUtils.appendZero(
						integer.substring(0, integer.length() - (i - 1) * 4),
						4, true);
			}
			integerSum.append(BinaryUtils.toHex(String.valueOf(BinaryUtils
					.binaryIntToDecimalism(binary))));
		}
		return integerSum.reverse().toString();
	}

	/**
	 * 将二进制小数部分转换成十六进制
	 * 
	 * @param xs
	 *            二进制字符串
	 * @return 转换后的十六进制字符串
	 */
	public static String binaryDecToHexadecimal(String decimals) {
		StringBuffer decimalsSum = new StringBuffer();
		int loop = 0;
		if (decimals.length() % 3 == 0) {
			loop = decimals.length() / 3;
		} else {
			loop = decimals.length() / 3 + 1;
		}
		String binary = "";
		for (int i = 1; i <= loop; i++) {
			if (i != loop) {
				binary = decimals.substring(4 * (i - 1), 4 * (i - 1) + 4);
			} else {
				binary = BinaryUtils.appendZero(decimals.substring(4 * (i - 1)),
						4, false);
			}
			decimalsSum.append(BinaryUtils.toHex(String.valueOf(BinaryUtils
					.binaryIntToDecimalism(binary))));
		}
		return decimalsSum.toString();
	}

	/**
	 * 将二进制转换成十六进制
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return 转换后的十六进制字符串
	 */
	public static String binaryToHexadecimal(String binary) {
		String integer = "";
		String point = "";
		String decimals = "";
		String integerSum = "";
		String decimalsSum = "";
		if (BinaryUtils.isBinary(binary)) {
			if (BinaryUtils.isContainsPoint(binary)) {
				integer = binary.substring(0, binary.indexOf("."));
				point = ".";
				decimals = binary.substring(binary.indexOf(".") + 1,
						binary.length());
				integerSum = BinaryUtils.binaryIntToHexadecimal(integer);
				decimalsSum = BinaryUtils.binaryDecToHexadecimal(decimals);
			} else {
				integerSum = BinaryUtils.binaryIntToHexadecimal(binary);
			}
		} else {
			try {
				throw new IllegalBinaryException(binary + " Illegal binary!");
			} catch (IllegalBinaryException be) {
				System.out.println(be.getMessage());
			}
		}
		StringBuilder sum = new StringBuilder();
		sum = sum.append(integerSum).append(point).append(decimalsSum);
		return sum.toString();
	}

	/**
	 * 将十进制整数部分转换成二进制
	 * 
	 * @param integer
	 *            十进制整数部分
	 * @return 转换后的二进制
	 */
	public static String decimalismIntToBinary(String integer) {
		return Integer.toBinaryString(Integer.parseInt(integer)).toString();
	}

	/**
	 * 将十进制小数部分转换成二进制
	 * 
	 * @param sxs
	 *            十进制整小数部分
	 * @return 转换后的二进制
	 */
	public static String decimalismDecToBinary(String decimals) {
		String pre = "0.";
		String all = pre + decimals;
		String sum = "";
		double dou = Double.parseDouble(all);
		while (!String.valueOf(dou).equals("0.0")) {
			dou = dou * 2;
			sum += String.valueOf(dou).substring(0,
					String.valueOf(dou).indexOf("."));
			dou = Double.parseDouble("0."
					+ String.valueOf(dou).substring(
							String.valueOf(dou).indexOf(".") + 1));
		}
		return sum;
	}

	/**
	 * 将十进制转换成二进制
	 * 
	 * @param decimalism
	 *            十进制数字符串
	 * @return 转换后的二进制数字符串
	 */
	public static String decimalismToBinary(String decimalism) {
		String binary = "";
		String point = "";
		String integer = "";
		String decimals = "";
		if (BinaryUtils.isNumber(decimalism)) {
			if (BinaryUtils.isContainsPoint(decimalism)) {
				integer = decimalism.substring(0, decimalism.indexOf("."));
				integer = BinaryUtils.decimalismIntToBinary(integer);
				point = ".";
				decimals = decimalism.substring(decimalism.indexOf(".") + 1);
				decimals = BinaryUtils.decimalismDecToBinary(decimals);
			} else {
				integer = BinaryUtils.decimalismIntToBinary(decimalism);
			}
		} else {
			try {
				throw new IllegalNumberException(decimalism
						+ " Illegal number!");
			} catch (IllegalNumberException be) {
				System.out.println(be.getMessage());
			}
		}
		binary = integer + point + decimals;
		return binary;
	}

	/**
	 * 将10~15转换成A~F
	 * 
	 * @param binary
	 *            十六进制字符串
	 * @return 转换后的十六进制数值
	 */
	public static String toHex(String hex) {
		String str = "";
		switch (Integer.parseInt(hex)) {
		case 10:
			str = "A";
			break;
		case 11:
			str = "B";
			break;
		case 12:
			str = "C";
			break;
		case 13:
			str = "D";
			break;
		case 14:
			str = "E";
			break;
		case 15:
			str = "F";
			break;
		default:
			str = hex;
		}
		return str;
	}

	/**
	 * 根据补位标志将源字符串补位到指定长度
	 * 
	 * @param str
	 *            源字符串
	 * @param len
	 *            补位到指定长度
	 * @param flag
	 *            补位标志 true-左补;false-右补
	 * @return 补位后的字符串
	 */
	public static String appendZero(String str, int len, boolean flag) {
		String zero = "0";
		if (null == str || str.length() == 0) {
			return "";
		}
		if (str.length() >= len) {
			return str;
		}
		for (int i = str.length(); i < len; i++) {
			if (flag) {
				str = zero + str;
			} else {
				str += zero;
			}
		}
		return str;
	}

	/**
	 * 是否合法二进制字符串
	 * 
	 * @param binary
	 *            二进制字符串
	 * @return true-合法;false-不合法
	 */
	public static boolean isBinary(String binary) {
		boolean flag = true;
		if (binary.contains(".")) {
			if (binary.lastIndexOf(".") + 1 == binary.length()) {
				return false;
			} else if (binary.indexOf(".") == 0) {
				return false;
			}
			char[] c = binary.toCharArray();
			int sum = 0;
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '.') {
					sum += 1;
				} else {
					if (c[i] != '0' && c[i] != '1') {
						return false;
					}
				}
				if (sum > 1) {
					return false;
				}
			}
		} else {
			char[] c = binary.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] != '0' && c[i] != '1') {
					return false;
				}
			}
		}
		return flag;
	}

	/**
	 * 是否包含小数点
	 * 
	 * @param number
	 *            字符串
	 * @return true-包含;false-不包含
	 */
	public static boolean isContainsPoint(String number) {
		return number.contains(".") ? true : false;
	}

	/**
	 * 判断是否数字
	 * 
	 * @param number
	 *            要判断的数字
	 * @return true-数字;false-非数字
	 */
	public static boolean isOToN(String number) {
		Pattern p = Pattern.compile("\\d");
		Matcher m = p.matcher(number);
		return m.matches();
	}

	/**
	 * 判断是否是一个合法的数字
	 * 
	 * @param number
	 *            要判断是数字
	 * @return true-合法数字;false-非法数字
	 */
	public static boolean isNumber(String number) {
		boolean flag = true;
		if (number.contains(".")) {
			if (number.lastIndexOf(".") + 1 == number.length()) {
				return false;
			} else if (number.indexOf(".") == 0) {
				return false;
			}
			char[] c = number.toCharArray();
			int sum = 0;
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '.') {
					sum += 1;
				} else {
					if (!BinaryUtils.isOToN(String.valueOf(c[i]))) {
						return false;
					}
				}
				if (sum > 1) {
					return false;
				}
			}
		} else {
			char[] c = number.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (!BinaryUtils.isOToN(String.valueOf(c[i]))) {
					return false;
				}
			}
		}
		return flag;
	}

	public static void main(String[] args) throws Exception {
		String binary = "110011";
		System.out.println(BinaryUtils.binaryToDecimalism(binary));
		System.out.println(BinaryUtils.binaryToOctal(binary));
		System.out.println(BinaryUtils.binaryToHexadecimal(binary));
		String integer = "51";
		System.out.println(BinaryUtils.decimalismToBinary(integer));

		String bin = "101011.101";
		System.out.println(BinaryUtils.binaryToDecimalism(bin));
		System.out.println(BinaryUtils.binaryToOctal(bin));
		System.out.println(BinaryUtils.binaryToHexadecimal(bin));
		String inte = "43.625";
		System.out.println(BinaryUtils.decimalismToBinary(inte));
	}
}
