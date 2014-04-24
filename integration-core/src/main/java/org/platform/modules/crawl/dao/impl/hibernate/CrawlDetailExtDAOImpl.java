package org.platform.modules.crawl.dao.impl.hibernate;

import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.crawl.dao.ICrawlDetailExtDAO;
import org.platform.modules.crawl.entity.CrawlDetailExt;
import org.springframework.stereotype.Repository;

@Repository("crawlDetailExtDAO")
public class CrawlDetailExtDAOImpl extends GenericHibernateDAOImpl<CrawlDetailExt, Long> 
	implements ICrawlDetailExtDAO {

}
