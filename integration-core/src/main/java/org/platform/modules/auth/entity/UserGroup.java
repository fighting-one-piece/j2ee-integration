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
@Table(name="T_PLAT_USER_GROUP")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserGroup extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**用户ID */
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="USER_ID")
	private User user = null;
	/**组ID */
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="GROUP_ID")
	private Group group = null;
	/**优先权*/
	@Column(name="PRIORITY")
	private String priority = null;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
