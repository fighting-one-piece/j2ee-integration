package org.platform.modules.abstr.common;

public enum Sort {

	HOT("hot", Sort.DESC), 
	TOP("top", Sort.DESC),
	NEW("time", Sort.DESC), 
	DATE("date", Sort.DESC),
	SCORE("score", Sort.DESC),
	RISING("rising", Sort.DESC),
	FAVOURABLE("favourable", Sort.DESC),
	CONFIDENCE("confidence", Sort.DESC),
	CONTROVERSIAL("controversial", Sort.DESC),
	
	POSTS_NUM("postsNum", Sort.DESC),
	SUBSCRIBE_NUM("subscribeNum", Sort.DESC);

	public static final int ASC = 1;
	
	public static final int DESC = 2;

	private String name = null;

	private int order;
	
	private Sort(String name, int order) {
		this.name = name;
		this.order = order;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOrder() {
		return order;
	}
	
	public static Sort convert(String orderBy) {
		Sort sort = Sort.HOT;
		if (orderBy.equalsIgnoreCase(Sort.TOP.getName())) {
			sort = Sort.TOP;
		} else if (orderBy.equalsIgnoreCase(Sort.NEW.getName())) {
			sort = Sort.NEW;
		} else if (orderBy.equalsIgnoreCase(Sort.DATE.getName())) {
			sort = Sort.DATE;
		} else if (orderBy.equalsIgnoreCase(Sort.SCORE.getName())) {
			sort = Sort.SCORE;
		} else if (orderBy.equalsIgnoreCase(Sort.RISING.getName())) {
			sort = Sort.RISING;
		} else if (orderBy.equalsIgnoreCase(Sort.CONFIDENCE.getName())) {
			sort = Sort.CONFIDENCE;
		} else if (orderBy.equalsIgnoreCase(Sort.CONTROVERSIAL.getName())) {
			sort = Sort.CONTROVERSIAL;
		} else if (orderBy.equalsIgnoreCase(Sort.POSTS_NUM.getName())) {
			sort = Sort.POSTS_NUM;
		} else if (orderBy.equalsIgnoreCase(Sort.SUBSCRIBE_NUM.getName())) {
			sort = Sort.SUBSCRIBE_NUM;
		}
		return sort;
	}
	
}
