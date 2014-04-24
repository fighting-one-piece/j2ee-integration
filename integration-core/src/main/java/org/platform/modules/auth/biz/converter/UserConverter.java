package org.platform.modules.auth.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.auth.dto.UserDTO;
import org.platform.modules.auth.entity.User;

public class UserConverter extends ConverterAbstrImpl<User, UserDTO> {

	private UserConverter(){};

	private static class UserConverterHolder{
		private static UserConverter instance = new UserConverter();
	}

	public static IConverter<User, UserDTO> getInstance() {
		return UserConverterHolder.instance;
	}
	

}
