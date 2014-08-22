package org.platform.modules.doc.controller;

import javax.annotation.Resource;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.controller.GenericController;
import org.platform.modules.doc.biz.IDocBusiness;
import org.platform.modules.doc.entity.Doc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/doc")
public class DocController extends GenericController<Doc, Long> {

	@Resource(name = "docBusiness")
	private IDocBusiness docBusiness = null;
	
	@Override
	public IGenericBusiness<Doc, Long> obtainBusinessInstance() {
		return docBusiness;
	}

	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	public String recommend(Model model) {
		return defaultViewPrefix() + "/recommend";
	}



}
