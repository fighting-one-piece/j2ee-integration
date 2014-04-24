package org.platform.modules.auth.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.auth.dto.GroupDTO;
import org.platform.modules.auth.entity.Group;

public class GroupConverter extends ConverterAbstrImpl<Group, GroupDTO> {

	private GroupConverter(){};

	private static class GroupConverterHolder{
		private static GroupConverter instance = new GroupConverter();
	}

	public static IConverter<Group, GroupDTO> getInstance() {
		return GroupConverterHolder.instance;
	}
	

}
