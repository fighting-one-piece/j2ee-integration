package org.platform.modules.crawl.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name = "T_PLAT_CRAWL_DETAIL")
@Access(AccessType.FIELD)
public class CrawlDetail extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;
    /** URL*/
	@Column(name = "URL")
	private String url = null;
	/** 类型*/
	@Column(name = "TYPE")
	private String type = null;
	/** 起始时间*/
	@Column(name = "START_TIME")
	private Date startTime = null;
	/** 结束时间*/
	@Column(name = "END_TIME")
	private Date endTime = null;
	/** 规则*/
	@Column(name = "RULE")
	private String rule = null;
	/** 状态*/
	@Column(name = "STATUS")
	private Integer status = null;
	/** 深度*/
	@Column(name = "LEVEL")
	private int level = 0;
	/** TOP_N*/
	@Column(name = "TOP_N")
	private int topN = 0;
	/** 上级URL*/
	@Column(name = "PARENT_URL")
	private String parentUrl = null;
	/** 细节扩展*/
	@OneToMany(mappedBy="crawlDetail", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<CrawlDetailExt> crawlDetailExts = null;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTopN() {
		return topN;
	}
	public void setTopN(int topN) {
		this.topN = topN;
	}
	public Set<CrawlDetailExt> getCrawlDetailExts() {
		if (null == crawlDetailExts) {
			crawlDetailExts = new HashSet<CrawlDetailExt>();
		}
		return crawlDetailExts;
	}
	public void setCrawlDetailExts(Set<CrawlDetailExt> crawlDetailExts) {
		this.crawlDetailExts = crawlDetailExts;
	}
	
	
	
	
	
}

