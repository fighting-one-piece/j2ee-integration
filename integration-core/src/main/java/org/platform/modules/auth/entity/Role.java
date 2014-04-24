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
@Table(name="T_PLAT_ROLE")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**角色名称 */
	@Column(name="NAME")
	private String name = null;
	/**角色标识 */
	@Column(name="IDENTITY")
	private String identity = null;
	/**角色描述 */
	@Column(name="DESCRIPTION")
	private String desc = null;
	/**角色有效标志 */
	@Column(name="AVAILAN")
	private Boolean availan = Boolean.FALSE;
	/**用户角色集合 */
	@OneToMany(mappedBy="role", fetch=FetchType.LAZY)
	private Set<UserRole> userRoles = null;
	/**组角色集合 */
	@OneToMany(mappedBy="role", fetch=FetchType.LAZY)
	private Set<GroupRole> groupRoles= null;
	/**角色菜单集合 */
	@OneToMany(mappedBy="role", cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<RoleResource> roleResources = null;

	public String getName() {
		return this.name;
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
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getAvailan() {
		return this.availan;
	}

	public void setAvailan(Boolean availan) {
		this.availan = availan;
	}

	public Set<UserRole> getUserRoles() {
		if (null == userRoles) {
			userRoles = new HashSet<UserRole>();
		}
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
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

	public Set<RoleResource> getRoleResources() {
		if (null == roleResources) {
			roleResources = new HashSet<RoleResource>();
		}
		return this.roleResources;
	}

	public void setRoleResources(Set<RoleResource> roleResources) {
		this.roleResources = roleResources;
	}

}
