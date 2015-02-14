package org.platform.utils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.platform.modules.abstr.common.annotation.ThingDataTable;
import org.platform.modules.abstr.common.annotation.ThingRelationTable;
import org.platform.utils.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/** 数据库表生成器*/
public class TableGen {
	
	private static final Logger LOG = LoggerFactory.getLogger(TableGen.class);
	
	public static final String THING_PREFIX = "db_thing_";

	public static final String DATA_PREFIX = "db_data_";
	
	public static final String RELATION_PREFIX = "db_relation_";
	
	public static final String INDEX_PREFIX = "index_";
	
	public static final String UNIQUE_PREFIX = "unique_";
	
	private ComboPooledDataSource dataSource;
	
	public TableGen() {
		this.dataSource = SpringContext.get("dataSource", ComboPooledDataSource.class);
	}
	
	public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void execute(String sql) {
		if (null == sql) return;
		Connection connection = null;
		PreparedStatement pstat = null;
		try {
			connection = dataSource.getConnection();
			pstat = connection.prepareStatement(sql);
			pstat.execute();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			close(connection, pstat, null);
		}
	}
	
	public void close(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
			if (null != statement) {
				statement.close();
			}
			if (null != connection) {
				connection.close();
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private String thingTable(String tableName) {
		return THING_PREFIX + tableName;
	}
	
	private String dataTable(String tableName) {
		return DATA_PREFIX + tableName;
	}
	
	private String relationTable(String tableName) {
		return RELATION_PREFIX + tableName;
	}
	
	private String indexName(String tableName, String name) {
		return INDEX_PREFIX + tableName + "_" + name;
	}
	
	private String uniqueName(String tableName) {
		return UNIQUE_PREFIX + tableName;
	}
	
	private String appendThingTable(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS ").append(thingTable(tableName)).append(" (");
		sb.append(" ID BIGINT NOT NULL AUTO_INCREMENT,");
		sb.append(" UPS INT,");
		sb.append(" DOWNS INT,");
		sb.append(" DELETED BOOLEAN,");
		sb.append(" SPAM BOOLEAN,");
		sb.append(" CREATOR_ID BIGINT,");
		sb.append(" CREATE_TIME TIMESTAMP,");
		sb.append(" PRIMARY KEY (ID))");
		String createTableString = sb.toString();
		LOG.info("CREATE SENTENCE : " + createTableString);
		return createTableString;
	}
	
	private String appendDataTable(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS ").append(dataTable(tableName)).append(" (");
		sb.append(" ID BIGINT NOT NULL AUTO_INCREMENT,");
		sb.append(" THING_ID BIGINT NOT NULL,");
		sb.append(" ATTRIBUTE VARCHAR(250) NOT NULL,");
		sb.append(" VALUE VARCHAR(10000),");
		sb.append(" KIND VARCHAR(50),");
		sb.append(" PRIMARY KEY (ID),");
		sb.append(" UNIQUE KEY ").append(uniqueName(tableName));
		sb.append(" (THING_ID, ATTRIBUTE))");
		String createTableString = sb.toString();
		LOG.info("CREATE SENTENCE : " + createTableString);
		return createTableString;
	}
	
	private String appendRelationTable(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS ").append(relationTable(tableName)).append(" (");
		sb.append(" ID BIGINT NOT NULL AUTO_INCREMENT,");
		sb.append(" THING1_ID BIGINT NOT NULL,");
		sb.append(" THING2_ID BIGINT NOT NULL,");
		sb.append(" KIND VARCHAR(200) NOT NULL,");
		sb.append(" DELETED BOOLEAN,");
		sb.append(" SPAM BOOLEAN,");
		sb.append(" CREATE_TIME TIMESTAMP,");
		sb.append(" INFO VARCHAR(5000),");
		sb.append(" PRIMARY KEY (ID),");
		sb.append(" UNIQUE KEY ").append(uniqueName(tableName));
		sb.append(" (THING1_ID, THING2_ID, KIND))");
		String createTableString = sb.toString();
		LOG.info("CREATE SENTENCE : " + createTableString);
		return createTableString;
	}
	
	private String appendIndex(int type, String tableName, String name, String on) {
		String table = null;
		switch (type) {
			case 1 : table = thingTable(tableName);break;
			case 2 : table = dataTable(tableName);break;
			case 3 : table = relationTable(tableName);break;
			default : table = thingTable(tableName);break;
		}
		String indexName = indexName(tableName, name);
		if (isExistIndex(table, indexName)) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE INDEX ").append(indexName);
		sb.append(" ON ").append(table);
		sb.append(" ( ").append(on).append(" ) "); 
		String createIndexString = sb.toString();
		LOG.info("CREATE SENTENCE : " + createIndexString);
		return createIndexString;
	}
	
	private boolean isExistIndex(String table, String indexName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SHOW INDEX FROM ").append(table);
		sql.append(" WHERE KEY_NAME = '").append(indexName).append("'");
		Connection connection = null;
		PreparedStatement pstat = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			pstat = connection.prepareStatement(sql.toString());
			resultSet = pstat.executeQuery();
			if (null != resultSet && resultSet.first()) {
				LOG.info("table: " + table + " index: " + indexName + " existed");
				return true;
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			close(connection, pstat, resultSet);
		}
		return false;
	}
	
	private void executeThingDataIndex(String tableName) {
		execute(appendIndex(1, tableName, "id", "id"));
		execute(appendIndex(1, tableName, "creator_id", "creator_id"));
		execute(appendIndex(1, tableName, "create_time", "create_time"));
		execute(appendIndex(1, tableName, "deleted", "deleted"));
		execute(appendIndex(1, tableName, "spam", "spam"));
		execute(appendIndex(2, tableName, "thing_id", "thing_id"));
		execute(appendIndex(2, tableName, "attribute", "attribute"));
		execute(appendIndex(2, tableName, "value", "value"));
	}
	
	private void executeThingRelationIndex(String tableName) {
		execute(appendIndex(3, tableName, "id", "id"));
		execute(appendIndex(3, tableName, "thing1_id", "thing1_id"));
		execute(appendIndex(3, tableName, "thing2_id", "thing2_id"));
		execute(appendIndex(3, tableName, "kind", "kind"));
		execute(appendIndex(3, tableName, "deleted", "deleted"));
	}
	
	public void run(String... paths) {
		try {
			for (String path : paths) {
				String packageName = path.replaceAll("/", ".");
				String dirPath = TableGen.class.getClassLoader().getResource(path).toURI().getPath();
				File dir = new File(dirPath);
				for (String fileName : dir.list()) {
					fileName = fileName.substring(0, fileName.indexOf("."));
					Class<?> clazz = Class.forName(packageName + "." + fileName);
					String tableName = fileName.toLowerCase();
					if (clazz.isAnnotationPresent(ThingDataTable.class)) {
						execute(appendThingTable(tableName));
						execute(appendDataTable(tableName));
						executeThingDataIndex(tableName);
					} else if (clazz.isAnnotationPresent(ThingRelationTable.class)) {
						execute(appendRelationTable(tableName));
						executeThingRelationIndex(tableName);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> paths = new ArrayList<String>();
		paths.add("com/netease/talk/modules/post/entity");
		paths.add("com/netease/talk/modules/qaa/entity");
		paths.add("com/netease/talk/modules/user/entity");
		paths.add("com/netease/talk/modules/inbox/entity");
		paths.add("com/netease/talk/modules/channel/entity");
		paths.add("com/netease/talk/modules/staticize/entity");
		paths.add("com/netease/talk/modules/push/entity");
		new TableGen().run(paths.toArray(new String[0]));
		System.exit(0);
	}
	
}
