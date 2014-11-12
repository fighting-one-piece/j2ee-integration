package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;

public class Thing implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(value = "id")
	private Long id = null;
	@Column(value = "ups")
	private Integer ups = 0;
	@Column(value = "downs")
	private Integer downs = 0;
	@Column(value = "deleted")
	private Boolean deleted = false;
	@Column(value = "spam")
	private Boolean spam = false;
	@Column(value = "date")
	private Date date = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUps() {
		return ups;
	}
	
	public void setUps(Integer ups) {
		this.ups = ups;
	}
	
	public Integer getDowns() {
		return downs;
	}
	
	public void setDowns(Integer downs) {
		this.downs = downs;
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
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date date() {
		return date;
	}
	
	public double hot() {
		return SortAlgorithm.hot(ups, downs, date);
	}
	
	public double score() {
		return SortAlgorithm.score(ups, downs);
	}
	
	public double controversy() {
		return SortAlgorithm.controversy(ups, downs);
	}
	
	public double confidence() {
		return SortAlgorithm.confidence(ups, downs);
	}
	
	public String identity() {
		return getClass().getSimpleName().toLowerCase() + "_" + id;
	}
	
}
