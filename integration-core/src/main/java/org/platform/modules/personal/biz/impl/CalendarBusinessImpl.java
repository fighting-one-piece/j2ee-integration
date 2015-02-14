package org.platform.modules.personal.biz.impl;

import java.sql.Time;
import java.util.Date;

import javax.annotation.Resource;

import org.platform.modules.abstr.biz.converter.IConverter;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.personal.biz.ICalendarBusiness;
import org.platform.modules.personal.biz.converter.CalendarConverter;
import org.platform.modules.personal.dao.ICalendarDAO;
import org.platform.modules.personal.entity.Calendar;
import org.springframework.stereotype.Service;

@Service("calendarBusiness")
public class CalendarBusinessImpl extends GenericBusinessImpl<Calendar, Long>
		implements ICalendarBusiness {

	@Resource(name = "calendarHibernateDAO")
	private ICalendarDAO calendarDAO = null;

	@Override
	public IGenericDAO<Calendar, Long> obtainDAOInstance() {
		return calendarDAO;
	}

	@Override
	protected IConverter<?, ?> obtainConverter() {
		return CalendarConverter.getInstance();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long countRecentlyCalendar(Long userId, Integer interval) {
		Date nowDate = new Date();
		Date nowTime = new Time(nowDate.getHours(), nowDate.getMinutes(),nowDate.getSeconds());
		nowDate.setHours(0);
		nowDate.setMinutes(0);
		nowDate.setSeconds(0);
		return calendarDAO.countRecentlyCalendar(userId, nowDate, nowTime, interval);
	}
}
