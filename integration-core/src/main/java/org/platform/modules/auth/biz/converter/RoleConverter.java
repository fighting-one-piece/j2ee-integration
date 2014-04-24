package org.platform.modules.auth.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.auth.dto.RoleDTO;
import org.platform.modules.auth.entity.Role;

public class RoleConverter extends ConverterAbstrImpl<Role, RoleDTO> {

	private RoleConverter(){};

	private static class RoleConverterHolder{
		private static RoleConverter instance = new RoleConverter();
	}

	public static IConverter<Role, RoleDTO> getInstance() {
		return RoleConverterHolder.instance;
	}
	

}
