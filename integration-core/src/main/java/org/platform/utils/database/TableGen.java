package org.platform.utils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.platform.modules.abstr.common.annotation.ThingDataTable;
import org.platform.modules.abstr.common.annotation.ThingRelationTable;
import org.platform.utils.spring.SpringUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/** 数据库表生成器*/
public class TableGen {
	
	private static Logger LOG = Logger.getLogger(TableGen.class);
	
	public static final String THING_PREFIX = "db_thing_";

	public static final String DATA_PREFIX = "db_data_";
	
	public static final String RELATION_PREFIX = "db_relation_";
	
	public static final String INDEX_PREFIX = "index_";
	
	public static final String UNIQUE_PREFIX = "unique_";
	
	private ComboPooledDataSource dataSource;
	
	public TableGen() {
		this.dataSource = SpringUtils.getBean("dataSource");
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
		sb.append(" create table if not exists ").append(thingTable(tableName)).append(" (");
		sb.append(" id bigint not null auto_increment,");
		sb.append(" ups int,");
		sb.append(" downs int,");
		sb.append(" deleted boolean,");
		sb.append(" spam boolean,");
		sb.append(" creator_id bigint,");
		sb.append(" create_time timestamp,");
		sb.append(" primary key (id))");
		String createTableString = sb.toString();
		LOG.info("create table: " + createTableString);
		return createTableString;
	}
	
	private String appendDataTable(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" create table if not exists ").append(dataTable(tableName)).append(" (");
		sb.append(" thing_id bigint not null,");
		sb.append(" attribute varchar(250) not null,");
		sb.append(" value varchar(5000),");
		sb.append(" kind varchar(50),");
		sb.append(" primary key (thing_id, attribute))");
		String createTableString = sb.toString();
		LOG.info("create table: " + createTableString);
		return createTableString;
	}
	
	private String appendRelationTable(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" create table if not exists ").append(relationTable(tableName)).append(" (");
		sb.append(" id bigint not null auto_increment,");
		sb.append(" thing1_id bigint not null,");
		sb.append(" thing2_id bigint not null,");
		sb.append(" kind varchar(200) not null,");
		sb.append(" deleted boolean,");
		sb.append(" spam boolean,");
		sb.append(" create_time timestamp,");
		sb.append(" info varchar(5000),");
		sb.append(" primary key (id),");
		sb.append(" unique key ").append(uniqueName(tableName));
		sb.append(" (thing1_id, thing2_id, kind))");
		String createTableString = sb.toString();
		LOG.info("create table: " + createTableString);
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
		sb.append(" create index ").append(indexName);
		sb.append(" on ").append(table);
		sb.append(" ( ").append(on).append(" ) "); 
		String createIndexString = sb.toString();
		LOG.info("create index: " + createIndexString);
		return createIndexString;
	}
	
	private boolean isExistIndex(String table, String indexName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" show index from ").append(table);
		sql.append(" where key_name = '").append(indexName).append("'");
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
		new TableGen().run(paths.toArray(new String[0]));
		System.exit(0);
	}
	
}
