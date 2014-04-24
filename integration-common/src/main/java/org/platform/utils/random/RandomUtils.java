package org.platform.utils.random;

import java.util.Random;
import java.util.UUID;

public class RandomUtils {

	public static String generateString(char[] chars, int length) {
		char[] result = new char[length];  
		Random random = new Random();
        for(int i = 0; i < length; i++){  
            int index = random.nextInt(chars.length);  
            result[i] = chars[index];  
        }  
        return new String(result);  
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
