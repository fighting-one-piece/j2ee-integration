package org.platform.modules.doc.dao.impl;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.doc.dao.IIndexDAO;
import org.platform.modules.doc.entity.Index;
import org.springframework.stereotype.Repository;

@Repository("indexHibernateDAO")
public class IndexDAOImpl extends GenericHibernateDAOImpl<Index, Long> implements IIndexDAO {

}
