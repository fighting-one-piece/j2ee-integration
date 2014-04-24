package org.platform.modules.crawl.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.crawl.dao.ICrawlDetailDAO;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.springframework.stereotype.Repository;

@Repository("crawlDetailDAO")
public class CrawlDetailDAOImpl extends GenericHibernateDAOImpl<CrawlDetail, Long>
	implements ICrawlDetailDAO {

}
