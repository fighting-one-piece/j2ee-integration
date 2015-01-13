package org.platform.modules.abstr.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Thing implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 标识*/
	private Long id = null;
	/** 顶数*/
	private int ups = 0;
	/** 踩数*/
	private int downs = 0;
	/** 删除标记*/
	private boolean deleted = false;
    /** 垃圾标记*/
	private boolean spam = false;
	/** 创建人标识*/
	private Long creatorId = null;
	/** 创建日期*/
	private Date createTime = null;
	/** DATA数据*/
	private transient List<ThingData> datas = null;
	/** 操作表名*/
	private transient String table = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUps() {
		return ups;
	}
	
	public void setUps(int ups) {
		this.ups = ups;
	}
	
	public int getDowns() {
		return downs;
	}
	
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		this.spam = spam;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public List<ThingData> getDatas() {
		if (null == datas) {
			datas = new ArrayList<ThingData>();
		}
		return datas;
	}

	public void setDatas(List<ThingData> datas) {
		this.datas = datas;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String identity() {
		return getClass().getSimpleName().toLowerCase() + ":" + id;
	}
	
	public void initBasicAttributes() {
		this.setUps(0);
		this.setDowns(0);
		this.setDeleted(false);
		this.setSpam(false);
		this.setCreateTime(new Date());
	}
	
}
