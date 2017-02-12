package org.platform.modules.auth.mapper;

import org.platform.modules.abstr.mapper.GenericMapper;
import org.platform.modules.auth.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userMapper")
public interface UserMapper extends GenericMapper<User, Long> {

	
	
}
