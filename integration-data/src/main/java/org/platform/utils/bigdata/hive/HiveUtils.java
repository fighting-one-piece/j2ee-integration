package org.platform.utils.bigdata.hive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.hadoop.hive.service.HiveClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.platform.utils.bigdata.AbstrUtils;
import org.platform.utils.common.jdbc.ConnectionPool;
import org.platform.utils.common.properties.PropertiesUtils;

public class HiveUtils extends AbstrUtils {
	
	private static TTransport transport = null;
	
	private static TProtocol protocol = null;
	//连接池
	private static ConnectionPool connectionPool = null;
	
	static{
		try{
			Properties properties = PropertiesUtils.obtainValues("jdbc/jdbc.properties");
			connectionPool = new ConnectionPool(properties.getProperty("hive.driverClassName"), 
					properties.getProperty("hive.url"), "", "");
			transport = new TSocket(properties.getProperty("hive.host.ip"),
					Integer.parseInt(properties.getProperty("hive.host.port")));
			protocol = new TBinaryProtocol(transport);
			transport.open();
		}catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	//获取连接池的连接
	public static synchronized Connection obtainConnection() {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
		}
		return connection;
	}
	
	public static synchronized HiveClient obtainHiveClient() {
		return new HiveClient(protocol);
	}
	
	public static void executeJDBC(String sql, Object... params) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = obtainConnection();
			pstmt = conn.prepareStatement(sql);
			if(null != params){
				for(int i = 0; i < params.length; i++){
					pstmt.setObject(i+1, params[i]);
				}
			}
			pstmt.executeQuery();
		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
		} finally {
			release(conn, pstmt, null);
		}
	}
	
	public static void executeClient(String sql) {
		try {
			obtainHiveClient().execute(sql);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		} 
	}
	
	/**
	 * 释放连接
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void release(Connection conn, Statement st, ResultSet rs) {
		try{
			if (null != rs) rs.close();
			if (null != st) st.close();
			if (null != conn) conn.close();
		}catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	/**
	 * 返回连接到连接池
	 * @param conn
	 */
	public static void returnConnection (Connection conn) {
		try{
			if (null != conn) connectionPool.returnConnection(conn);
		}catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}

}
