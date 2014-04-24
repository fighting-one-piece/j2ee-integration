package org.platform.modules.auth.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name="T_PLAT_USER_ROLE")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserRole extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**用户ID */
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="USER_ID")
	private User user = null;
	/**角色ID */
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="ROLE_ID")
	private Role role = null;
	/**优先权*/
	@Column(name="PRIORITY")
	private String priority = null;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
