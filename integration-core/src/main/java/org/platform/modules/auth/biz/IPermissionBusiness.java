package org.platform.modules.auth.biz;

import java.util.List;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.Permission;
import org.platform.utils.exception.BusinessException;

public interface IPermissionBusiness extends IGenericBusiness<Permission, Long> {

	/**
	 * 根据标识类型和标识ID获取权限列表
	 * @param principalType
	 * @param principalId
	 * @return
	 * @throws BusinessException
	 */
	public List<Permission> readPermissionsByPrincipalTypeAndPrincipalId(
			Integer principalType, Long principalId) throws BusinessException;
	
}
