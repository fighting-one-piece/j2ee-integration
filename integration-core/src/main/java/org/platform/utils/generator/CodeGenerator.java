package org.platform.utils.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.platform.utils.template.ITemplateEngine;
import org.platform.utils.template.TemplateFactory;

public class CodeGenerator {

	private Logger logger = Logger.getLogger(CodeGenerator.class);

	public static final String PROJECT_DIR = System.getProperty("user.dir");

	public static final String PACKAGE_PREFIX = "/src/main/java/platform/module/";

	public static final String PACKAGE_DAO_SUFFIX = "/dao/";

	public static final String PACKAGE_BIZ_SUFFIX = "/biz/";

	public static final String PACKAGE_IMPL = "impl/";

	public static final String FILE_SUFFIX = ".java";

	private static CodeGenerator instance = null;

	private CodeGenerator() {
	}

	public static synchronized CodeGenerator getInstance() {
		if (null == instance) {
			instance = new CodeGenerator();
		}
		return instance;
	}

	public void execute(String moduleName, String entityName) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("module", moduleName);
		dataMap.put("entity", entityName);
		createDaoFile(dataMap, moduleName, entityName);
		createDaoImplFile(dataMap, moduleName, entityName);
		createBizFile(dataMap, moduleName, entityName);
		createBizImplFile(dataMap, moduleName, entityName);
	}

	private void createDaoFile(Map<String, Object> dataMap, String moduleName, String entityName) {
		TemplateFactory factory = TemplateFactory.getInstance();
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_FREEMARKER);
		String daoContent = engine.getTemplate(dataMap, "StandardDAO.ftl");
		String daoFileDir = PROJECT_DIR + PACKAGE_PREFIX + moduleName + PACKAGE_DAO_SUFFIX;
		String daoFileName = "I" + entityName + "DAO" + FILE_SUFFIX;
		writeFile(daoContent, daoFileDir, daoFileName);
	}

	private void createDaoImplFile(Map<String, Object> dataMap, String moduleName, String entityName) {
		TemplateFactory factory = TemplateFactory.getInstance();
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_FREEMARKER);
		String daoImplContent = engine.getTemplate(dataMap, "StandardDAOImpl.ftl");
		String daoImplFileDir = PROJECT_DIR + PACKAGE_PREFIX + moduleName + PACKAGE_DAO_SUFFIX + PACKAGE_IMPL;
		String daoImplFileName = entityName + "DAOImpl" + FILE_SUFFIX;
		writeFile(daoImplContent, daoImplFileDir, daoImplFileName);
	}

	private void createBizFile(Map<String, Object> dataMap, String moduleName, String entityName) {
		TemplateFactory factory = TemplateFactory.getInstance();
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_FREEMARKER);
		String bizContent = engine.getTemplate(dataMap, "StandardBusiness.ftl");
		String bizFileDir = PROJECT_DIR + PACKAGE_PREFIX + moduleName + PACKAGE_BIZ_SUFFIX;
		String bizFileName = "I" + entityName + "Business" + FILE_SUFFIX;
		writeFile(bizContent, bizFileDir, bizFileName);
	}

	private void createBizImplFile(Map<String, Object> dataMap, String moduleName, String entityName) {
		TemplateFactory factory = TemplateFactory.getInstance();
		ITemplateEngine engine = factory.getEngine(TemplateFactory.TEMPLATE_ENGINE_FREEMARKER);
		String bizImplContent = engine.getTemplate(dataMap, "StandardBusinessImpl.ftl");
		String bizImplFileDir = PROJECT_DIR + PACKAGE_PREFIX + moduleName + PACKAGE_BIZ_SUFFIX + PACKAGE_IMPL;
		String bizImplFileName = entityName + "BusinessImpl" + FILE_SUFFIX;
		writeFile(bizImplContent, bizImplFileDir, bizImplFileName);
	}

	private void writeFile(String content, String fileDir, String fileName) {
		InputStream inStream = new ByteArrayInputStream(content.getBytes());
		OutputStream outStream = null;
		try {
			File dir = new File(fileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			outStream = new FileOutputStream(new File(dir, fileName));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buff)) != -1) {
				outStream.write(buff, 0, len);
			}
			outStream.flush();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.debug(e.getMessage(), e);
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					logger.debug(e.getMessage(), e);
				}
			}
		}
	}
}
