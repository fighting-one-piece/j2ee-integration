package org.platform.modules.auth.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IPermissionDAO;
import org.platform.modules.auth.entity.Permission;
import org.springframework.stereotype.Repository;

@Repository("permissionHibernateDAO")
public class PermissionDAOImpl extends GenericHibernateDAOImpl<Permission, Long> implements IPermissionDAO {

}
