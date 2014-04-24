package org.platform.modules.crawl.entity;

public class CrawlJob {

	/** 职位名称*/
	private String career = null;
	/** 公司名称*/
	private String company = null;
	/** 工作地点*/
	private String location = null;
	/** 更新日*/
	private String updateTime = null;
	/** 职位要求*/
	private String require = null;
	/** 职位简介*/
	private String summary = null;
	/** 链接地址*/
	private String link = null;

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRequire() {
		return require;
	}

	public void setRequire(String require) {
		this.require = require;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("career: ").append(this.career).append("----");
		sb.append("company: ").append(this.company).append("----");
		sb.append("location: ").append(this.location).append("----");
		sb.append("updateTime: ").append(this.updateTime).append("----");
		sb.append("require: ").append(this.require).append("----");
		sb.append("summary: ").append(this.summary).append("----");
		return sb.toString();
	}
	
}
