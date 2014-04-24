package org.platform.modules.personal.dao.impl.hibernate;

import java.util.Date;

import org.hibernate.Query;
import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.personal.dao.ICalendarDAO;
import org.platform.modules.personal.entity.Calendar;
import org.springframework.stereotype.Repository;

@Repository("calendarHibernateDAO")
public class CalendarDAOImpl extends GenericHibernateDAOImpl<Calendar, Long> implements ICalendarDAO {

	@Override
	public Long countRecentlyCalendar(Long userId, Date nowDate, Date nowTime, Integer interval) {
		String hql = "select count(id) from Calendar where userId=:userId and ((startDate=:startDate and (startTime is null or startTime<:endTime)) or (startDate>:startDate and startDate<=(:startDate+:interval)) or (startDate<:startDate and (startDate+length)>:startDate) or ((startDate+length)=:startDate and (endTime is null or endTime>:endTime)))";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("startDate", nowDate);
		query.setParameter("endTime", nowTime);
		query.setParameter("interval", interval);
		return ((Number) query.uniqueResult()).longValue();
	}

}
