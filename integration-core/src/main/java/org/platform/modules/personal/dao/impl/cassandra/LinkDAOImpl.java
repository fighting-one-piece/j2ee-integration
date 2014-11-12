package org.platform.modules.personal.dao.impl.cassandra;

import org.platform.modules.abstr.dao.impl.GenericCassandraDAOImpl;
import org.platform.modules.personal.dao.ILinkDAO;
import org.platform.modules.personal.entity.Link;
import org.springframework.stereotype.Repository;

@Repository("linkDAO")
public class LinkDAOImpl extends GenericCassandraDAOImpl<Link, Long> implements ILinkDAO {

}
