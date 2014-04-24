package org.platform.modules.auth.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.auth.dto.ResourceDTO;
import org.platform.modules.auth.entity.Resource;

public class ResourceConverter extends ConverterAbstrImpl<Resource, ResourceDTO> {

	private ResourceConverter(){};

	private static class ResourceConverterHolder{
		private static ResourceConverter instance = new ResourceConverter();
	}

	public static IConverter<Resource, ResourceDTO> getInstance() {
		return ResourceConverterHolder.instance;
	}
	

}
