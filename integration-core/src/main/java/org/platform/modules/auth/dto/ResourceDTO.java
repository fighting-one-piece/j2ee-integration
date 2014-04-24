package org.platform.modules.auth.dto;

import java.io.Serializable;

public class ResourceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 资源ID*/
	private Long id = null;
	/** 资源名称*/
	private String name = null;
	/** 资源标识*/
	private String code = null;
	/** 资源URL*/
	private String url = null;
	/** 资源排序*/
	private String sortCode = null;
	/** 有效标志 */
	private String availan = null;
	/** 父资源*/
	private Long parentId = null;

	public ResourceDTO() {
	}

	public ResourceDTO(Long id, String name, String code, String url,
			String sortCode, String availan, Long parentId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.url = url;
		this.sortCode = sortCode;
		this.availan = availan;
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getAvailan() {
		return availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

}
