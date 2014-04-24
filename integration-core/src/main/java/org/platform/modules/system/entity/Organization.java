package org.platform.modules.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name="T_PLAT_ORGANIZATION")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/** 机构名称*/
	@Column(name = "NAME")
	private String name = null;
	/** 机构编码*/
	@Column(name = "CODE")
	private String code = null;
	/** 机构描述*/
	@Column(name="DESCRIPTION")
	private String desc = null;
	/** 父机构*/
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name="PARENT_ID")
	private Organization parent = null;
	/** 子机构*/
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
	private Set<Organization> children = null;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
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

	public Organization getParent() {
		return this.parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public Set<Organization> getChildren() {
		if (children == null) {
			children = new HashSet<Organization>();
		}
		return this.children;
	}

	public void setChildren(Set<Organization> children) {
		this.children = children;
	}

}
