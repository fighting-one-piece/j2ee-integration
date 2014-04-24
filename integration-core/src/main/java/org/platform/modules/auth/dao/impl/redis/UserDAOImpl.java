package org.platform.modules.auth.dao.impl.redis;

import org.platform.modules.abstr.dao.impl.GenericRedisDAOImpl;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.platform.utils.exception.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

@Repository("userRedisDAO")
public class UserDAOImpl extends GenericRedisDAOImpl<User, Long> implements IUserDAO {

	@Override
	public void insert(final User user) throws DataAccessException {
		redisTemplate.execute(new RedisCallback<User>() {
			@Override
			public User doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				connection.set(redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + user.getId()), 
						redisTemplate.getStringSerializer().serialize(user.getName()));
				return null;
			}
		});
	}
	
	@Override
	public User readDataByPK(final Long pk) throws DataAccessException {
		return redisTemplate.execute(new RedisCallback<User>() {
			@Override
			public User doInRedis(RedisConnection connection)
					throws org.springframework.dao.DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(
						entityClass.getName() + "." + pk);
				if (connection.exists(key)) {
					String name = redisTemplate.getStringSerializer().deserialize(
							connection.get(key));
					User user = new User();
					user.setName(name);
					return user;
				}
				return null;
			}
		});
	}
}
