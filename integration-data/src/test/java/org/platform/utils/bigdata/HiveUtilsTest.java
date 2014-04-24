package org.platform.utils.bigdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;
import org.platform.utils.bigdata.hive.HiveUtils;
import org.platform.utils.common.resource.ResourceManager;

public class HiveUtilsTest {

	@Test
	public void testHiveJdbcExecute() throws Exception {
		String sql = "select * from pokes";
		Connection conn = HiveUtils.obtainConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet resultSet = pstmt.executeQuery();
		while (resultSet.next()) {
			System.out.println(resultSet.getObject(2));
		}
		HiveUtils.release(conn, pstmt, resultSet);
	}
	
	@Test
	public void testHiveCreate() throws Exception {
		String sql = "create table t_plat_test(id int, title string, content string)";
		HiveUtils.executeJDBC(sql);
	}
	
	@Test
	public void testHiveClientExecute() {
		String sql = "insert overwrite directory '/user/hadoop/pokes' select * from pokes";
		HiveUtils.executeClient(sql);
	}
	
	@Test
	public void initTable() throws Exception {
		String movieHqlPath = ResourceManager.getAbsolutePath("hive/movie.hql");
		InputStream in = new FileInputStream(new File(movieHqlPath));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("--")) {
					continue;
				} else if (line.endsWith(";")) {
					sb.append(line);
					sb.deleteCharAt(sb.length() - 1);
					System.out.println(sb.toString());
					HiveUtils.executeClient(sb.toString());
					sb = new StringBuilder();
				} else {
					sb.append(line).append(" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void initTempTable() throws Exception {
		String movieHqlPath = ResourceManager.getAbsolutePath("hive/movie_temp.hql");
		InputStream in = new FileInputStream(new File(movieHqlPath));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("--")) {
					continue;
				} else if (line.endsWith(";")) {
					sb.append(line);
					sb.deleteCharAt(sb.length() - 1);
					System.out.println(sb.toString());
					HiveUtils.executeClient(sb.toString());
					sb = new StringBuilder();
				} else {
					sb.append(line).append(" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
