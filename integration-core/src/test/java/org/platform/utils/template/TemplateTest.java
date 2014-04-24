package org.platform.utils.template;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.platform.utils.generator.CodeGenerator;
import org.platform.utils.template.ITemplateEngine;
import org.platform.utils.template.TemplateFactory;

public class TemplateTest {

	private TemplateFactory factory = null;
	private Map<String, Object> data = null;

	@Before
	public void init() {
		factory = TemplateFactory.getInstance();
	}

	@Test
	public void testFreeMarkerTemplate() {
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_FREEMARKER);
		data = new HashMap<String, Object>();
		data.put("module", "test");
		data.put("entity", "User");
		System.out.println(engine.getTemplate(data, "StandardDAO.ftl"));
	}

	@Test
	public void testVelocityTemplate() {
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_VELOCITY);
		data = new HashMap<String, Object>();
		System.out.println(engine.getTemplate(data, "standard.vm"));
	}

	@Test
	public void testCodeGenerator() {
		CodeGenerator.getInstance().execute("hr", "Organization");
	}
}
