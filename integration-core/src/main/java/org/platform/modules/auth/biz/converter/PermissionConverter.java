package org.platform.modules.auth.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.auth.dto.PermissionDTO;
import org.platform.modules.auth.entity.Permission;

public class PermissionConverter extends ConverterAbstrImpl<Permission, PermissionDTO> {

	private PermissionConverter(){};

	private static class PermissionConverterHolder{
		private static PermissionConverter instance = new PermissionConverter();
	}

	public static IConverter<Permission, PermissionDTO> getInstance() {
		return PermissionConverterHolder.instance;
	}
	

}
