package org.platform.modules.auth.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name = "T_PLAT_GROUP")
@Access(AccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/** 组名称 */
	@Column(name = "NAME")
	private String name = null;
	/** 组标识 */
	@Column(name="IDENTITY")
	private String identity = null;
	/** 组描述 */
	@Column(name = "DESCRIPTION")
	private String desc = null;
	/** 组有效标志 */
	@Column(name = "AVAILAN")
	private Boolean availan = Boolean.FALSE;
	/**用户组集合 */
	@OneToMany(mappedBy="group", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<UserGroup> userGroups = null;
	/**组角色集合 */
	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	private Set<GroupRole> groupRoles = null;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getAvailan() {
		return availan;
	}

	public void setAvailan(Boolean availan) {
		this.availan = availan;
	}

	public Set<GroupRole> getGroupRoles() {
		if (null == groupRoles) {
			groupRoles = new HashSet<GroupRole>();
		}
		return groupRoles;
	}

	public void setGroupRoles(Set<GroupRole> groupRoles) {
		this.groupRoles = groupRoles;
	}

	public Set<UserGroup> getUserGroups() {
		if (null == userGroups) {
			userGroups = new HashSet<UserGroup>();
		}
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
	

}
