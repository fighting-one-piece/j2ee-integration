package org.platform.utils.template;

import java.util.Map;

import org.apache.log4j.Logger;

public abstract class TemplateEngineAbstrImpl implements ITemplateEngine {

	protected Logger logger = Logger.getLogger(getClass());

	public abstract String getTemplatePath();

	@Override
	public String getTemplate(Map<String, Object> data, String name) {
		return null;
	}

}
