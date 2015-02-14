package org.platform.modules.abstr.biz;

import java.io.Serializable;

import org.platform.utils.exception.BusinessException;

public interface IGenericTRBusiness<Entity extends Serializable, PK extends Serializable> extends IGenericBusiness<Entity, PK> {

	
	/**
	 * 根据主键修改删除标记
	 * @param pk
	 * @param deleted
	 * @throws BusinessException
	 */
	public void updateDeleted(PK pk, boolean deleted) throws BusinessException;
	
	/**
	 * 根据主键修改垃圾标记
	 * @param pk
	 * @param spam
	 * @throws BusinessException
	 */
	public void updateSpam(PK pk, boolean spam) throws BusinessException;
	
}
