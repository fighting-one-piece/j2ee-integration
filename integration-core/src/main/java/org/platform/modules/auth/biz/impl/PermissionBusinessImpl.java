package org.platform.modules.auth.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.biz.IPermissionBusiness;
import org.platform.modules.auth.biz.converter.PermissionConverter;
import org.platform.modules.auth.dao.IPermissionDAO;
import org.platform.modules.auth.entity.Permission;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("permissionBusiness")
public class PermissionBusinessImpl extends GenericBusinessImpl<Permission, Long> implements IPermissionBusiness {
	
	@Resource(name = "permissionHibernateDAO")
	private IPermissionDAO permissionHibernateDAO = null;
	
	@Override
	protected IGenericDAO<Permission, Long> obtainDAOInstance() {
		return permissionHibernateDAO;
	}
	
	@Override
	protected IConverter<?, ?> obtainConverter() {
		return PermissionConverter.getInstance();
	}
	
	@Override
	public List<Permission> readPermissionsByPrincipalTypeAndPrincipalId(
			Integer principalType, Long principalId) throws BusinessException {
		Query condition = new Query();
		condition.addHibernateCondition("principalType", principalType);
		condition.addHibernateCondition("principalId", principalId);
		return permissionHibernateDAO.readDataListByCondition(condition);
	}
}
