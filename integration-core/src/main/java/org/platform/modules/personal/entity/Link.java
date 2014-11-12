package org.platform.modules.personal.entity;

import org.platform.modules.abstr.dao.cassandra.thingdb.Thing;

public class Link extends Thing {

	private static final long serialVersionUID = 1L;

	private String title = null;

	private String url = null;
    
	private Long rId = null;
	
	private Long authorId = null;
	
	private String ip = null;
	
	private Integer commentsNum = null;
	
	private Integer reported = null;
	
	private Integer commentTreeId = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getrId() {
		return rId;
	}

	public void setrId(Long rId) {
		this.rId = rId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(Integer commentsNum) {
		this.commentsNum = commentsNum;
	}

	public Integer getReported() {
		return reported;
	}

	public void setReported(Integer reported) {
		this.reported = reported;
	}

	public Integer getCommentTreeId() {
		return commentTreeId;
	}

	public void setCommentTreeId(Integer commentTreeId) {
		this.commentTreeId = commentTreeId;
	}
	
	
}
