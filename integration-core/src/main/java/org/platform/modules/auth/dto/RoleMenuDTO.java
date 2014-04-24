package org.platform.modules.auth.dto;

import java.io.Serializable;

public class RoleMenuDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long roleId;

	private String roleName;

	private Long menuId;

	private String menuName;

	private String menuCode;

	private String menuUrl;

	private String menuImg;

	private String availan;

	private String sortCode;

	private Long parentMenuId;

	public RoleMenuDTO(Long roleId,String roleName ,Long menuId, String menuName,
			String menuCode, String menuUrl, String menuImg, String availan,
			String sortCode) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuCode = menuCode;
		this.menuUrl = menuUrl;
		this.menuImg = menuImg;
		this.availan = availan;
		this.sortCode = sortCode;
	}

	public RoleMenuDTO(Long roleId,String roleName ,Long menuId, String menuName,
			String menuCode, String menuUrl, String menuImg, String availan,
			String sortCode,Long parentMenuId) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuCode = menuCode;
		this.menuUrl = menuUrl;
		this.menuImg = menuImg;
		this.availan = availan;
		this.sortCode = sortCode;
		this.parentMenuId = parentMenuId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuImg() {
		return menuImg;
	}

	public void setMenuImg(String menuImg) {
		this.menuImg = menuImg;
	}

	public String getAvailan() {
		return availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

}