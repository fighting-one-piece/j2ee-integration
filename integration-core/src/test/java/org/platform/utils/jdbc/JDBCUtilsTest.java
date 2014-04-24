package org.platform.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.platform.modules.auth.entity.User;
import org.platform.utils.random.RandomUtils;

public class JDBCUtilsTest {

	@Test
	public void testInsert() {
		String sql = "insert into t_plat_user(name,password) values (?,?)";
		JDBCUtils.execute(sql, new Object[]{"hadoop", "hadoop"});
	}
	
	@Test
	public void testInsertBatch() throws Exception {
		Connection conn = JDBCUtils.obtainConnection();
		String sql = "insert into t_plat_rank(user,score) values(?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		Random random = new Random();
		for (int i = 0; i < 100000; i++) {
			pstmt.setString(1, RandomUtils.generateUUID());
			pstmt.setInt(2, random.nextInt(100000));
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
	
	@Test
	public void testReadOne() {
		String sql = "select * from t_plat_user where name = ?";
		User user = (User) JDBCUtils.execute(sql, new BeanResultSetHandler(User.class), "admin");
		System.out.println(user.getPassword());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadList() {
		String sql = "select * from t_plat_user where name like ?";
		List<User> users = (List<User>) JDBCUtils.execute(sql, new BeansResultSetHandler(User.class), "test%");
		System.out.println("size: " + users.size());
		for (User user : users) {
			System.out.println(user.getPassword());
		}
	}
	
}
