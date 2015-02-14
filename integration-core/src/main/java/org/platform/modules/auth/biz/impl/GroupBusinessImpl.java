package org.platform.modules.auth.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.biz.IGroupBusiness;
import org.platform.modules.auth.biz.converter.GroupConverter;
import org.platform.modules.auth.dao.IGroupDAO;
import org.platform.modules.auth.entity.Group;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("groupBusiness")
public class GroupBusinessImpl extends GenericBusinessImpl<Group, Long> implements IGroupBusiness {
	
	@Resource(name = "groupHibernateDAO")
	private IGroupDAO groupHibernateDAO = null;
	
	public IGenericDAO<Group, Long> obtainDAOInstance() {
		return groupHibernateDAO;
	}
	
	@Override
	protected IConverter<?, ?> obtainConverter() {
		return GroupConverter.getInstance();
	}

	@Override
	public List<Group> readDataListByUserId(Long userId) throws BusinessException {
		return groupHibernateDAO.readDataListByUserId(userId);
	}
}
