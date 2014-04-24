package org.platform.utils.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.platform.utils.jdbc.JDBCUtils;
import org.platform.utils.resource.ResourceManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer implements InitializingBean {
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void afterPropertiesSet() throws Exception {
		initializing();
	}
	
	public void initializing() {
		Properties props = ResourceManager.getProperties("database/database.properties");
		boolean initialTable = Boolean.parseBoolean(props.getProperty("initialTable"));
		boolean initialData = Boolean.parseBoolean(props.getProperty("initialData"));
		boolean initialIndex = Boolean.parseBoolean(props.getProperty("initialIndex"));
		if (initialTable) {
			execute("db_table_mysql.sql");
		}
		if (initialData) {
			execute("db_data_mysql.sql");
		}
		if (initialIndex) {
			String indexColumnNames = props.getProperty("indexColumnNames");
			System.out.println("indexColumnNames: " + indexColumnNames);
			initialIndex((String[]) (indexColumnNames.contains(",") ? indexColumnNames.split(",") : indexColumnNames));				
		}
	}
	
	private void execute(String fileName) {
		InputStream in = DBInitializer.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("--")) {
					continue;
				} else if (line.endsWith(";")) {
					sb.append(line).append(" ").append("\n");
					logger.debug(sb.toString());
					JDBCUtils.execute(sb.toString());
					sb = new StringBuilder();
				} else {
					sb.append(line).append(" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initialIndex(String... columnNames) {
		try {
			DatabaseMetaData dbMetaData = JDBCUtils.obtainConnection().getMetaData();
			 //可为:"TABLE","VIEW","SYSTEM TABLE","GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"    
			String[] types = {"TABLE"};
			ResultSet tableResultSet = dbMetaData.getTables(null, null, null, types);
			while (tableResultSet.next()) {
				String tableName = tableResultSet.getString("TABLE_NAME");
				System.out.println("table: " + tableName);
				for (String columnName : columnNames) {
					ResultSet columnResultSet = dbMetaData.getColumns(null, null, tableName, columnName);
					if (columnResultSet.next()) {
						System.out.println(tableName + " has column " + columnResultSet.getString("COLUMN_NAME"));
						handleIndex(dbMetaData, tableName, columnName);
					} else {
						System.out.println(tableName + " no column " + columnName);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void handleIndex(DatabaseMetaData dbMetaData, String tableName, String columnName) {
		try {
			ResultSet indexResultSet = dbMetaData.getIndexInfo(null, null, tableName, false, false);
			boolean isExist = false;
			while (indexResultSet.next()) {
				String indexColumnName = indexResultSet.getString("COLUMN_NAME").toLowerCase();
				System.out.println(tableName + " has index column : " + indexColumnName);
				if (columnName.equals(indexColumnName)) {
					isExist = true;
					break;
				}
			}
			if (isExist) {
				System.out.println(tableName + "has existed index column: " + columnName);
			} else {
				StringBuilder sql = new StringBuilder();
				sql.append("ALTER TABLE ").append(tableName).append(" ADD INDEX ")
						.append(obtainIndexName(tableName)).append(" (").append(columnName).append(") ");
				System.out.println("index sql: " + sql.toString());
				JDBCUtils.execute(sql.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Integer> indexNameMap = new HashMap<String, Integer>();
	
	private String obtainIndexName(String tableName) {
		Integer value = null == indexNameMap.get(tableName) ? 0 : (indexNameMap.get(tableName) + 1);
		indexNameMap.put(tableName, value);
		return tableName + "_" + value;	
	}
	
}
