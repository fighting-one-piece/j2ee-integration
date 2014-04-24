package org.platform.modules.crawl.dto;

import java.io.Serializable;
import java.util.Date;

public class CrawlDetailDTO  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url = null;
	private Date startTime = null;
	private Date endTime = null;
	private Integer status = null;
	private String parentUrl = null;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	
	
}

