package org.platform.modules.auth.biz;

import java.util.List;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.Group;
import org.platform.utils.exception.BusinessException;

public interface IGroupBusiness extends IGenericBusiness<Group, Long> {

	/**
	 * 根据用户ID读取组列表
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<Group> readDataListByUserId(Long userId) throws BusinessException;
}
