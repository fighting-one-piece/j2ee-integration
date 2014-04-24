package org.platform.modules.crawl.entity;

public enum CrawlDetailStatus {
	
	SUCCESS(1),FAILURE(2),UNCRAWL(3),CRAWLING(4);
	
	private int value = 0;
	
	private CrawlDetailStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
	
}
