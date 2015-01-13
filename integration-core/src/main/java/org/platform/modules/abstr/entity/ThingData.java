package org.platform.modules.abstr.entity;

import java.io.Serializable;

public class ThingData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** Thing表标识*/
	private Long thingId = null;
	/** 属性名称*/
	private String attribute = null;
	/** 属性值*/
	private String value = null;
	/** 属性值类型*/
	private String kind = null;
	/** 操作表名字*/
	private transient String table = null;

	public Long getThingId() {
		return thingId;
	}

	public void setThingId(Long thingId) {
		this.thingId = thingId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void printToConsole() {
		System.out.print("thingId: " + thingId);
		System.out.print(" attribute: " + attribute);
		System.out.print(" value: " + value);
		System.out.println(" kind: " + kind);
	}
	
}
