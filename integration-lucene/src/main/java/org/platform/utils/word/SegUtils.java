package org.platform.utils.word;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;

public class SegUtils {

	private static Dictionary dic = Dictionary.getInstance();
	
	private static Seg complexSeg = null;
	
	private static Seg maxWordSeg = null;
	
	static {
		complexSeg = new ComplexSeg(dic);
		maxWordSeg = new MaxWordSeg(dic);
	}
	
	public static Seg getComplexSeg() {
		return complexSeg;
	}
	
	public static Seg getMaxWordSeg() {
		return maxWordSeg;
	}
	
}
