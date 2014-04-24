package org.platform.modules.auth.dto;

import java.io.Serializable;


public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID*/
	private Long id = null;
	/** 组名称 */
	private String name = null;
	/** 组编码 */
	private String code = null;
	/** 组描述 */
	private String desc = null;
	/** 组有效标志 */
	private String availan = null;

	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAvailan() {
		return availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

}
