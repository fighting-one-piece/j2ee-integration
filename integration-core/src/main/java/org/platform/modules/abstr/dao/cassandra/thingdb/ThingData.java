package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.io.Serializable;

import org.springframework.data.cassandra.mapping.Column;

public class ThingData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(value = "thing_id")
	private Long thingId = null;
	@Column(value = "key")
	private String key = null;
	@Column(value = "value")
	private String value = null;
	@Column(value = "kind")
	private String kind = null;

	public Long getThingId() {
		return thingId;
	}

	public void setThingId(Long thingId) {
		this.thingId = thingId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
	
	public void printToConsole() {
		System.out.print("thingId: " + thingId);
		System.out.print(" key: " + key);
		System.out.print(" value: " + value);
		System.out.println(" kind: " + kind);
	}
	
}
