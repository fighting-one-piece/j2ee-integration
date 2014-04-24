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
import org.platform.modules.auth.entity.RoleResource;

@Entity
@Table(name = "T_PLAT_MENU")
@Access(AccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**菜单名称 */
	@Column(name = "NAME")
	private String name = null;
	/**菜单编码 */
	@Column(name = "CODE")
	private String code = null;
	/**菜单URL */
	@Column(name = "URL")
	private String url = null;
	/**菜单图标 */
	@Column(name = "IMG")
	private String img = null;
	/**菜单有效标志 */
	@Column(name = "AVAILAN")
	private String availan = null;
	/**菜单排序 */
	@Column(name = "SORT_CODE")
	private String sortCode = null;
	/**父菜单*/
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true)
	@JoinColumn(name="MENU_ID")
	private Menu parent = null;
	/**子菜单集*/
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<Menu> children = null;
	/** 角色菜单*/
	@OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
	private Set<RoleResource> roleResource = null;

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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Menu getParent() {
		return this.parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public String getAvailan() {
		return this.availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

	public String getSortCode() {
		return this.sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public Set<Menu> getChildren() {
		if (children == null) {
			children = new HashSet<Menu>();
		}
		return this.children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}

	public Set<RoleResource> getRoleResource() {
		return roleResource;
	}

	public void setRoleResource(Set<RoleResource> roleResource) {
		this.roleResource = roleResource;
	}


}
