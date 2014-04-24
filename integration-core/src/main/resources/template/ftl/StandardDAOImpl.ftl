package org.platform.modules.${module}.dao.impl;

import org.springframework.stereotype.Repository;

import org.platform.modules.abstr.dao.GenericDAOImpl;
import org.platform.modules.${module}.entity.${entity};
import org.platform.modules.${module}.dao.I${entity}DAO;

@Repository("${entity ? uncap_first}DAO")
public class ${entity}DAOImpl extends GenericDAOImpl<${entity}, Long> implements I${entity}DAO {

}