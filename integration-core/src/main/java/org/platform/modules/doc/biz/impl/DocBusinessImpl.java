package org.platform.modules.doc.biz.impl;

import javax.annotation.Resource;

import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.doc.biz.IDocBusiness;
import org.platform.modules.doc.dao.IDocDAO;
import org.platform.modules.doc.entity.Doc;
import org.springframework.stereotype.Service;

@Service("docBusiness")
public class DocBusinessImpl extends GenericBusinessImpl<Doc, Long> implements IDocBusiness{
	
	@Resource(name = "docMyBatisDAO")
	private IDocDAO docDAO = null;
	
	@Override
	protected IGenericDAO<Doc, Long> obtainDAOInstance() {
		return docDAO;
	}
}