package org.platform.modules.index.dao.impl;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.index.dao.IIndexDAO;
import org.platform.modules.index.entity.Index;
import org.springframework.stereotype.Repository;

@Repository("indexHibernateDAO")
public class IndexDAOImpl extends GenericHibernateDAOImpl<Index, Long> implements IIndexDAO {

}
