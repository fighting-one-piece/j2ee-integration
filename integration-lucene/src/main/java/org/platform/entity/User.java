package org.platform.entity;

import java.util.Date;

public class User extends PKEntity<Long> {

	private static final long serialVersionUID = 1L;

	private Long id = null;
	private String name = null;
	private String password = null;
	private String code = null;
	private Date createTime = null;
	private Date expireTime = null;
	private String availan = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getAvailan() {
		return this.availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

	@Override
	public Long getPK() {
		return null;
	}

	@Override
	public void setPK(Long pk) {
		
	}


}
