package org.platform.modules.auth.biz.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.biz.IGroupBusiness;
import org.platform.modules.auth.biz.IRoleBusiness;
import org.platform.modules.auth.biz.converter.RoleConverter;
import org.platform.modules.auth.dao.IRoleDAO;
import org.platform.modules.auth.entity.Group;
import org.platform.modules.auth.entity.Role;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("roleBusiness")
public class RoleBusinessImpl extends GenericBusinessImpl<Role, Long> implements IRoleBusiness {

	@Resource(name = "roleHibernateDAO")
	private IRoleDAO roleHibernateDAO = null;
	
	@Resource(name = "groupBusiness")
	private IGroupBusiness groupBusiness = null;
	
	@Override
	protected IGenericDAO<Role, Long> obtainDAOInstance() {
		return roleHibernateDAO;
	}
	
	@Override
	protected IConverter<?, ?> obtainConverter() {
		return RoleConverter.getInstance();
	}
	
	@Override
	public List<Role> readDataListByUserId(Long userId, boolean containGroup) throws BusinessException {
		Set<Role> roles = new HashSet<Role>();
		List<Role> userRoles = roleHibernateDAO.readDataListByUserId(userId);
		roles.addAll(userRoles);
		if (containGroup) {
			List<Group> groups = groupBusiness.readDataListByUserId(userId);
	        for (Group group : groups) {
	        	List<Role> groupRoles = readDataListByGroupId(group.getId());
	        	roles.addAll(groupRoles);
	        }
		}
		return new ArrayList<Role>(roles);
	}
	
	@Override
	public List<Role> readDataListByGroupId(Long groupId) throws BusinessException {
		return roleHibernateDAO.readDataListByGroupId(groupId);
	}
}
