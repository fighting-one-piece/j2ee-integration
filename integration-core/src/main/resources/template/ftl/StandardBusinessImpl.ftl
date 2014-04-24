package org.platform.modules.${module}.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.platform.modules.abstr.biz.GenericBusinessImpl;
import org.platform.modules.abstr.biz.convert.ConvertObjectAbstractImpl;
import org.platform.modules.${module}.entity.${entity};
import org.platform.modules.${module}.biz.I${entity}Business;

@Service("${entity ? uncap_first}Business")
public class ${entity}BusinessImpl extends GenericBusinessImpl<${entity}, Long> implements I${entity}Business {

}