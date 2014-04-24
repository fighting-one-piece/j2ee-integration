package org.platform.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalConstants {

	public static final String SM_INVALIDDATE = "SM_INVALIDDATE";

	public static final String DATASOURCE_TYPE_JNDI = "jndi";

	public static final String DATASOURCE_TYPE_JDBC = "jdbc";

	public static final String DATASOURCE_TYPE_DBCP = "dataSourceDBCP";
	public static final String DATASOURCE_TYPE_C3P0 = "dataSourceC3P0CP";
	public static final String DATASOURCE_TYPE_BONECP = "dataSourceBoneCP";
	public static final String DATASOURCE_TYPE_PROXOOL = "dataSourceProxoolCP";

	public static final String RETURN_SUCCESS_CODE = "success";
	public static final String RETURN_FAILURE_CODE = "failure";

	public static final String GLOBAL_VARIABLE_GLOBAL_CONTEXT = "globalContext";
	public static final String GLOBAL_VARIABLE_CURRENT_USER = "user";
	public static final String GLOBAL_VARIABLE_USER_ROLE = "userRole";
	public static final String GLOBAL_VARIABLE_ROLE_MENU = "roleMenu";
	public static final String GLOBAL_VARIABLE_RANDOM_NUMBER = "randomNumber";
	public static final String GLOBAL_VARIABLE_AVAILAN_FALSE = "0";
	public static final String GLOBAL_VARIABLE_AVAILAN_TRUE = "1";

	public static final String PROJECT_DIR = System.getProperty("user.dir");

	public static Map<String, String> EXCEL_USER_INFO;

	static {
		EXCEL_USER_INFO = new LinkedHashMap<String, String>();
	}
}
