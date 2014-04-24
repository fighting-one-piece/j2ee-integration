package org.platform.modules.auth.biz.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryItem;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.auth.biz.IResourceBusiness;
import org.platform.modules.auth.biz.converter.ResourceConverter;
import org.platform.modules.auth.dao.IResourceDAO;
import org.platform.modules.auth.entity.Resource;
import org.platform.utils.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service("resourceBusiness")
public class ResourceBusinessImpl extends GenericBusinessImpl<Resource, Long> implements IResourceBusiness {
	
	@javax.annotation.Resource(name = "resourceHibernateDAO")
	private IResourceDAO resourceDAO = null;
	
	@Override
	protected IGenericDAO<Resource, Long> obtainDAOInstance() {
		return resourceDAO;
	}
	
	@Override
	protected IConverter<?, ?> obtainConverter() {
		return ResourceConverter.getInstance();
	}
	
	@Override
	public String readActualIndentity(Long id) throws BusinessException {
		Resource resource = resourceDAO.readDataByPK(id);
		if (null == resource || Boolean.FALSE.equals(resource.getAvailan())) {
			return null;
		}
		StringBuilder sb = new StringBuilder(resource.getIdentity());
		boolean hasResourceIdentity = !StringUtils.isEmpty(resource.getIdentity());
		Resource parent = resourceDAO.readDataByPK(resource.getParentId());
		while (null != parent) {
			if(!StringUtils.isEmpty(parent.getIdentity())) {
                sb.insert(0, parent.getIdentity() + ":");
                hasResourceIdentity = true;
            }
			parent = resourceDAO.readDataByPK(parent.getParentId());
		}
		//如果用户没有声明资源标识且父也没有,那么就为空
		if(!hasResourceIdentity) {
            return "";
        }
		//如果最后一个字符是:因为不需要,所以删除之
        int length = sb.length();
        if(length > 0 && sb.lastIndexOf(":") == length - 1) {
            sb.deleteCharAt(length - 1);
        }
        //如果有子节点,最后拼一个*
        boolean hasChildren = false;
        QueryCondition condition = new QueryCondition();
        condition.addHibernateCondition("id", Resource.ROOT, QueryItem.MATCH_NE);
        List<Resource> allResources = resourceDAO.readDataListByCondition(condition);
        for(Resource r : allResources) {
            if(null != r.getParent() && resource.getId().equals(r.getParentId())) {
                hasChildren = true;
                break;
            }
        }
        if(hasChildren) {
            sb.append(":*");
        }
		return sb.toString();
	}
	
	@Override
	public List<Resource> readResourceTree(Integer type) throws BusinessException {
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("type", type);
		condition.addHibernateCondition("availan", Boolean.TRUE);
		condition.addHibernateCondition("parent.id", Resource.ROOT);
		List<Resource> topResources = resourceDAO.readDataListByCondition(condition);
		for (Resource topResource : topResources) {
			iterateHandle(topResource);
		}
		return topResources;
	}
	
	private void iterateHandle(Resource resource) {
		QueryCondition condition = new QueryCondition();
		condition.addHibernateCondition("type", Resource.TYPE_MENU);
		condition.addHibernateCondition("parent.id", resource.getId());
		List<Resource> subResources = resourceDAO.readDataListByCondition(condition);
		for (Resource subResource : subResources) {
			resource.getChildren().add(subResource);
			iterateHandle(subResource);
		}
	}
}
