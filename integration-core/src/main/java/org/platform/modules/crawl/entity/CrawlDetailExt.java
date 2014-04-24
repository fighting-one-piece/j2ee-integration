package org.platform.modules.crawl.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name = "T_PLAT_CRAWL_DETAIL_EXT")
@Access(AccessType.FIELD)
public class CrawlDetailExt extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;
    
	/** 键*/
	@Column(name = "DETAIL_KEY")
	private String key = null;
	/** 值*/
	@Column(name = "DETAIL_VALUE", length = 2000)
	private String value = null;
	/** 值扩展*/
	@Column(name = "DETAIL_VALUE_EXT")
	private byte[] valueExt = null;
	/** 细节*/
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="CRAWL_DETAIL_ID")
	private CrawlDetail crawlDetail = null;
	
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
	public byte[] getValueExt() {
		return valueExt;
	}
	public void setValueExt(byte[] valueExt) {
		this.valueExt = valueExt;
	}
	public CrawlDetail getCrawlDetail() {
		return crawlDetail;
	}
	public void setCrawlDetail(CrawlDetail crawlDetail) {
		this.crawlDetail = crawlDetail;
	}
	
}

