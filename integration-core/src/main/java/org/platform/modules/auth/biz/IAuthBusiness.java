package org.platform.modules.auth.biz;

import java.util.List;
import java.util.Set;

import org.platform.modules.auth.entity.Resource;
import org.platform.utils.exception.BusinessException;

public interface IAuthBusiness {

	/**
	 * 根据用户ID获取角色标识集合
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public Set<String> readRoleIdentitiesByUserId(Long userId) throws BusinessException;
	
	/**
	 * 根据用户ID获取权限允许集合
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public Set<String> readPermissionsByUserId(Long userId) throws BusinessException;
	
	/**
	 * 根据资源类型与用户ID获取资源集合
	 * @param type 资源类型
	 * @param userId 
	 * @return
	 * @throws BusinessException
	 */
	public List<Resource> readResourcesByTypeAndUserIdWithStrategyOne(int type, Long userId) throws BusinessException;
	
	/**
	 * 根据资源类型与用户ID获取资源集合
	 * @param type 资源类型
	 * @param userId 
	 * @return
	 * @throws BusinessException
	 */
	public List<Resource> readResourcesByTypeAndUserIdWithStrategyTwo(int type, Long userId) throws BusinessException;
}
