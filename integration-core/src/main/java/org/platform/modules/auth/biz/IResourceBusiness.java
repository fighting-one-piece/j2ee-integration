package org.platform.modules.auth.biz;

import java.util.List;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.auth.entity.Resource;
import org.platform.utils.exception.BusinessException;

public interface IResourceBusiness extends IGenericBusiness<Resource, Long> {
	
	/**
	 * 根据主键获取资源真正标识
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public String readActualIndentity(Long id) throws BusinessException; 
	
	/**
	 * 根据资源类型读取资源树形列表
	 * @return
	 * @throws BusinessException
	 */
	public List<Resource> readResourceTree(Integer type) throws BusinessException;

}
