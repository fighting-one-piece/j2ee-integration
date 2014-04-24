package org.platform.modules.personal.biz;

import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.personal.entity.Calendar;

public interface ICalendarBusiness extends IGenericBusiness<Calendar, Long> {

	public Long countRecentlyCalendar(Long userId, Integer interval);
}
