package org.platform.modules.doc.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.platform.modules.abstr.entity.PKAutoEntity;

@Entity
@Table(name="T_PLAT_INDEX")
@Access(AccessType.FIELD)
public class Index extends PKAutoEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** */
	@Column(name="TITLE")
	private String title = null;
	/** */
	@Column(name="CONTENT")
	private String content = null;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
