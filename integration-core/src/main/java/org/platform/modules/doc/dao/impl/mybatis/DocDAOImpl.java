package org.platform.modules.doc.dao.impl.mybatis;

import org.platform.modules.abstr.dao.impl.GenericMyBatisDAOImpl;
import org.platform.modules.doc.dao.IDocDAO;
import org.platform.modules.doc.entity.Doc;
import org.springframework.stereotype.Repository;

@Repository("docMyBatisDAO")
public class DocDAOImpl extends GenericMyBatisDAOImpl<Doc, Long> implements IDocDAO {

}
