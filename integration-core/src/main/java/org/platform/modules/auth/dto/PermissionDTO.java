package org.platform.modules.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID*/
	private Long id = null;
	/** 主体类型  0标识用户  1标识角色*/
	private Integer principalType = null;
	/** 主体标识*/
	private Long principalId = null;
	/** 资源ID*/
	private Long resourceId = null;
	/** 资源名称*/
	private String resourceName = null;
	/** 授权状态 用后四位标识CRUD操作*/
	private Integer authStatus = null;
	/** 继承状态  0标识不继承 1标识继承*/
	private Integer extendStatus = null;
	/** 新增*/
	private Integer created = null;
	/** 查看*/
	private Integer readed = null;
	/** 更新*/
	private Integer updated = null;
	/** 删除*/
	private Integer deleted = null;
	/** 继承*/
	private Integer extended = null;
	/** 父类ID*/
	private String parentId = null;
	/** 子类*/
	@SuppressWarnings("rawtypes")
	private List children = null;

	public PermissionDTO() {}

	@SuppressWarnings("rawtypes")
	public PermissionDTO(Long id, Integer principalType,
			Long principalId, Long resourceId, String resourceName,
			Integer authStatus, Integer extendStatus, Integer created,
			Integer readed, Integer updated, Integer deleted, Integer extended,
			String parentId, List children) {
		super();
		this.id = id;
		this.principalType = principalType;
		this.principalId = principalId;
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.authStatus = authStatus;
		this.extendStatus = extendStatus;
		this.created = created;
		this.readed = readed;
		this.updated = updated;
		this.deleted = deleted;
		this.extended = extended;
		this.parentId = parentId;
		this.children = children;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPrincipalType() {
		return principalType;
	}

	public void setPrincipalType(Integer principalType) {
		this.principalType = principalType;
	}

	public Long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public Integer getExtendStatus() {
		return extendStatus;
	}

	public void setExtendStatus(Integer extendStatus) {
		this.extendStatus = extendStatus;
	}

	public Integer getCreated() {
		return created;
	}

	public void setCreated(Integer created) {
		this.created = created;
	}

	public Integer getReaded() {
		return readed;
	}

	public void setReaded(Integer readed) {
		this.readed = readed;
	}

	public Integer getUpdated() {
		return updated;
	}

	public void setUpdated(Integer updated) {
		this.updated = updated;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getExtended() {
		return extended;
	}

	public void setExtended(Integer extended) {
		this.extended = extended;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@SuppressWarnings("rawtypes")
	public List getChildren() {
		if (children == null) {
			children = new ArrayList();
		}
		return children;
	}

	@SuppressWarnings("rawtypes")
	public void setChildren(List children) {
		this.children = children;
	}

}
