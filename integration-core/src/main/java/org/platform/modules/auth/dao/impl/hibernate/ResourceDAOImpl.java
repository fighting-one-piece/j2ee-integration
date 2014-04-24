package org.platform.modules.auth.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IResourceDAO;
import org.platform.modules.auth.entity.Resource;
import org.springframework.stereotype.Repository;

@Repository("resourceHibernateDAO")
public class ResourceDAOImpl extends GenericHibernateDAOImpl<Resource, Long> implements IResourceDAO {

}
