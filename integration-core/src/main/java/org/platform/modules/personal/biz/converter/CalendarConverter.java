package org.platform.modules.personal.biz.converter;

import org.platform.modules.abstr.biz.converter.ConverterAbstrImpl;
import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.personal.dto.CalendarDTO;
import org.platform.modules.personal.entity.Calendar;

public class CalendarConverter extends ConverterAbstrImpl<Calendar, CalendarDTO> {

	private CalendarConverter(){};

	private static class CalendarConverterHolder{
		private static CalendarConverter instance = new CalendarConverter();
	}

	public static IConverter<Calendar, CalendarDTO> getInstance() {
		return CalendarConverterHolder.instance;
	}
	

}
