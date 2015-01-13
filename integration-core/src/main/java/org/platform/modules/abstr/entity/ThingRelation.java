package org.platform.modules.abstr.entity;

import java.io.Serializable;
import java.util.Date;

public class ThingRelation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 标识*/
	private Long id = null;
	/** Thing1标识*/
	private Long thing1Id = null;
	/** Thing2标识*/
	private Long thing2Id = null;
	/** 关系类型*/
	private String kind = null;
	/** 删除标记*/
	private Boolean deleted = null;
	/** 垃圾标记*/
	private Boolean spam = null;
	/** 日期*/
	private Date createTime = null;
	/** 相关信息*/
	private String info = null;
	/** 操作表名字*/
	private transient String table = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getThing1Id() {
		return thing1Id;
	}

	public void setThing1Id(Long thing1Id) {
		this.thing1Id = thing1Id;
	}

	public Long getThing2Id() {
		return thing2Id;
	}

	public void setThing2Id(Long thing2Id) {
		this.thing2Id = thing2Id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getSpam() {
		return spam;
	}

	public void setSpam(Boolean spam) {
		this.spam = spam;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public void initBasicAttributes() {
		this.setKind(getClass().getSimpleName().toLowerCase());
		this.setDeleted(false);
		this.setSpam(false);
		this.setCreateTime(new Date());
	}
	
}
