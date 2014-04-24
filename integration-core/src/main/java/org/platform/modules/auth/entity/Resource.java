package org.platform.modules.auth.entity;

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
import org.hibernate.annotations.Formula;
import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name="T_PLAT_RESOURCE")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 根ID*/
	public static final Long ROOT = 1L;
	
	/** MENU*/
	public static final Integer TYPE_MENU = 1;

	/** 资源名称*/
	@Column(name="NAME")
	private String name = null;
	/** 资源类型*/
	@Column(name="TYPE")
	private Integer type = null;
	/** 资源标识*/
	@Column(name="IDENTITY")
	private String identity = null;
	/** 资源URL*/
	@Column(name="URL")
	private String url = null;
	/** 资源图标*/
	@Column(name="ICON")
	private String icon = null;
	/**资源优先权*/
	@Column(name="PRIORITY")
	private String priority = null;
	/** 有效标志 */
	@Column(name = "AVAILAN")
	private Boolean availan = Boolean.FALSE;
	/** 父资源*/
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name = "PARENT_ID")
	private Resource parent = null;
	/** 子资源*/
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
	private Set<Resource> children = null;
	/** 是否有子节点*/
	@Formula(value = "(select count(*) from t_plat_resource r where r.parent_id = id)")
	private boolean hasChildren;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Boolean getAvailan() {
		return availan;
	}

	public void setAvailan(Boolean availan) {
		this.availan = availan;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Set<Resource> getChildren() {
		if (children == null) {
			children = new HashSet<Resource>();
		}
		return children;
	}

	public void setChildren(Set<Resource> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}
	
	public boolean hasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	public Long getParentId() {
		return null == parent ? null : parent.getId();
	}
	
	public boolean isTop() {
		return null != this.getParent() && Resource.ROOT.equals(this.getParent().getId());
	}

}
