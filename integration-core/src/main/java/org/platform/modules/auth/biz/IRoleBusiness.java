package org.platform.modules.auth.biz;

import java.util.List;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.Role;
import org.platform.utils.exception.BusinessException;

public interface IRoleBusiness extends IGenericBusiness<Role, Long> {
	
	/**
	 * 根据用户ID获取是否包括组的角色列表
	 * @param userId
	 * @param containGroup
	 * @return
	 * @throws BusinessException
	 */
	public List<Role> readDataListByUserId(Long userId, boolean containGroup) throws BusinessException;
	
	/**
	 * 根据组ID获取角色列表
	 * @param groupId
	 * @return
	 * @throws BusinessException
	 */
	public List<Role> readDataListByGroupId(Long groupId) throws BusinessException;

}
