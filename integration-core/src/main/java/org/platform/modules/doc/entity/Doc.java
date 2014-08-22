package org.platform.modules.doc.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name="T_PLAT_DOC")
@Access(AccessType.FIELD)
public class Doc extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "AUTOID")
	private int autoid = 0;
	@Column(name = "DOCNO")
	private String docno = null;
	@Column(name = "TITLE")
	private String title = null;
	@Column(name = "URL")
	private String url = null;
	@Column(name = "SOURCE")
	private String source = null;
	@Column(name = "PTIME")
	private Date ptime = null;
	@Column(name = "CHANNEL")
	private String channel = null;
	@Column(name = "CREATETIME")
	private Date createtime = null;

	public int getAutoid() {
		return autoid;
	}

	public void setAutoid(int autoid) {
		this.autoid = autoid;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(String docno) {
		this.docno = docno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getPtime() {
		return ptime;
	}

	public void setPtime(Date ptime) {
		this.ptime = ptime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}
