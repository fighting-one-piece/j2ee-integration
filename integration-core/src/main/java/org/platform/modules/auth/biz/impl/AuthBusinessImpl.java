package org.platform.modules.auth.biz.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryItem;
import org.platform.modules.auth.biz.IAuthBusiness;
import org.platform.modules.auth.biz.IGroupBusiness;
import org.platform.modules.auth.biz.IPermissionBusiness;
import org.platform.modules.auth.biz.IResourceBusiness;
import org.platform.modules.auth.biz.IRoleBusiness;
import org.platform.modules.auth.biz.IUserBusiness;
import org.platform.modules.auth.entity.Group;
import org.platform.modules.auth.entity.Permission;
import org.platform.modules.auth.entity.Resource;
import org.platform.modules.auth.entity.Role;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("authBusiness")
public class AuthBusinessImpl implements IAuthBusiness {

	@javax.annotation.Resource(name = "userBusiness")
	private IUserBusiness userBusiness = null;

	@javax.annotation.Resource(name = "groupBusiness")
	private IGroupBusiness groupBusiness = null;

	@javax.annotation.Resource(name = "roleBusiness")
	private IRoleBusiness roleBusiness = null;
	
	@javax.annotation.Resource(name = "resourceBusiness")
	private IResourceBusiness resourceBusiness = null;

	@javax.annotation.Resource(name = "permissionBusiness")
	private IPermissionBusiness permissionBusiness = null;

	@Override
	public Set<String> readRoleIdentitiesByUserId(Long userId) throws BusinessException {
        List<Role> roles = roleBusiness.readDataListByUserId(userId, true);
        Set<String> roleIdentities = new HashSet<String>();
        for (Role role : roles) {
        	roleIdentities.add(role.getIdentity());
        }
		return roleIdentities;
	}

	@Override
	public Set<String> readPermissionsByUserId(Long userId) throws BusinessException {
		Set<Permission> allPermissions = new HashSet<Permission>();
		//用户权限
		Set<Role> roles = new HashSet<Role>();
        List<Role> userRoles = roleBusiness.readDataListByUserId(userId, false);
        roles.addAll(userRoles);
        List<Permission> userPermissions = permissionBusiness
    			.readPermissionsByPrincipalTypeAndPrincipalId(
    					Permission.PRINCIPAL_TYPE_USER, userId);
    	allPermissions.addAll(userPermissions);
        //组权限
        List<Group> groups = groupBusiness.readDataListByUserId(userId);
        for (Group group : groups) {
        	List<Role> groupRoles = roleBusiness.readDataListByGroupId(group.getId());
        	roles.addAll(groupRoles);
        	List<Permission> groupPermissions = permissionBusiness
        			.readPermissionsByPrincipalTypeAndPrincipalId(
        					Permission.PRINCIPAL_TYPE_GROUP, group.getId());
        	allPermissions.addAll(groupPermissions);
        }
        //角色权限
        for (Role role : roles) {
        	List<Permission> rolePermissions = permissionBusiness
        			.readPermissionsByPrincipalTypeAndPrincipalId(
        					Permission.PRINCIPAL_TYPE_ROLE, role.getId());
        	allPermissions.addAll(rolePermissions);
        	
        }
        
        Set<String> permissions = new HashSet<String>();
        for (Permission permission : allPermissions) {
        	String resourceIdentity = resourceBusiness.readActualIndentity(permission.getResourceId());
        	List<String> operateIdentities = permission.obtainOperateIdentities();
        	for (String operateIdentity : operateIdentities) {
        		permissions.add(resourceIdentity + ":" + operateIdentity);
        	}
        }
		return permissions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> readResourcesByTypeAndUserIdWithStrategyOne(int type, 
			Long userId) throws BusinessException {
		Set<String> userPermissions = readPermissionsByUserId(userId);
		
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("type", type);
		condition.addHibernateCondition("availan", Boolean.TRUE);
		List<Resource> allResources = (List<Resource>) 
				resourceBusiness.readDataListByCondition(condition, false);
		
		Iterator<Resource> resourceIterator = allResources.iterator();
		while (resourceIterator.hasNext()) {
			if (!hasPermission(resourceIterator.next(), userPermissions)) {
				resourceIterator.remove();
			}
		}
		return convert2Tree(allResources);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> readResourcesByTypeAndUserIdWithStrategyTwo(int type,
			Long userId) throws BusinessException {
		List<Role> roles = roleBusiness.readDataListByUserId(userId, true);
		Long[] roleIds = new Long[roles.size()];
		for (int i = 0, len = roles.size(); i < len; i++) {
			roleIds[i] = roles.get(i).getId();
		}
		
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("type", type);
		condition.addHibernateCondition("availan", Boolean.TRUE);
		condition.addHibernateCondition("parent.id", Resource.ROOT);
		List<Resource> topResources = (List<Resource>) 
				resourceBusiness.readDataListByCondition(condition, false);
		
		Iterator<Resource> resourceIterator = topResources.iterator();
		while (resourceIterator.hasNext()) {
			Resource topResource = resourceIterator.next();
			if (hasPermission(topResource, roleIds)) {
				iterateHandle(topResource, roleIds);
			} else {
				resourceIterator.remove();
			}
		}
		return topResources;
	}
	
	/**
	 * 判断资源是否在用户授权范围中
	 * @param resource
	 * @param userPermissions
	 * @return
	 */
	private boolean hasPermission(Resource resource, Set<String> userPermissions) {
        String actualResourceIdentity = resourceBusiness.readActualIndentity(resource.getId());
        if (StringUtils.isEmpty(actualResourceIdentity)) {
            return true;
        }

        for (String permission : userPermissions) {
            if (hasPermission(permission, actualResourceIdentity)) {
                return true;
            }
        }

        return false;
    }
	
	private boolean hasPermission(String permission, String actualResourceIdentity) {
        //得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
        String permissionResourceIdentity = permission.substring(0, permission.lastIndexOf(":"));

        //如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
        if(permissionResourceIdentity.startsWith(actualResourceIdentity)) {
            return true;
        }

        //模式匹配
        WildcardPermission p1 = new WildcardPermission(permissionResourceIdentity);
        WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);

        return p1.implies(p2) || p2.implies(p1);
    }
	
	@SuppressWarnings("unchecked")
	private boolean hasPermission(Resource resource, Long[] roleIds) {
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("principalType", Permission.PRINCIPAL_TYPE_ROLE);
		condition.addHibernateCondition("principalId", roleIds, QueryItem.MATCH_IN);
		condition.addHibernateCondition("resourceId", resource.getId());
		List<Permission> rolePermissions = (List<Permission>) 
				permissionBusiness.readDataListByCondition(condition, false);
		for (Permission rolePermission : rolePermissions) {
			if (rolePermission.hasPermission(Permission.AUTH_READ)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将资源列表转化为树形结构
	 * @param allResources
	 * @return
	 */
	private List<Resource> convert2Tree(List<Resource> allResources) {
		List<Resource> resultResources = new ArrayList<Resource>();
		for (Resource resource : allResources) {
			if (resource.isTop()) {
				resultResources.add(resource);
				iterateHandle(resource, allResources);
			}
		}
		return resultResources;
	}
	
	@SuppressWarnings("unchecked")
	private void iterateHandle(Resource resource, Long[] roleIds) {
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("type", Resource.TYPE_MENU);
		condition.addHibernateCondition("parent.id", resource.getId());
		List<Resource> subResources = (List<Resource>) 
				resourceBusiness.readDataListByCondition(condition, false);
		resource.setChildren(null);
		for (Resource subResource : subResources) {
			if (hasPermission(subResource, roleIds)) {
				resource.getChildren().add(subResource);
				iterateHandle(subResource, roleIds);
			}
		}
	}
	
	private void iterateHandle(Resource resultResource, List<Resource> allResources) {
		for (Resource resource : allResources) {
			if (resultResource.getId().equals(resource.getParentId())) {
				resultResource.getChildren().add(resource);
				iterateHandle(resource, allResources);
			}
		}
	}

}
