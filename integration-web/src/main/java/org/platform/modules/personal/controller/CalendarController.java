package org.platform.modules.personal.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ehcache.config.Searchable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.platform.entity.Query;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.web.controller.GenericController;
import org.platform.modules.auth.entity.User;
import org.platform.modules.front.annotation.CurrentUser;
import org.platform.modules.personal.biz.ICalendarBusiness;
import org.platform.modules.personal.entity.Calendar;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/admin/personal/calendar")
public class CalendarController extends GenericController<Calendar, Long> {

    private static final long oneDayMillis = 24L * 60 * 60 * 1000;
    private static final String dataFormat = "yyyy-MM-dd HH:mm:ss";

    @Resource(name = "calendarBusiness")
    private ICalendarBusiness calendarBusiness = null;
    
    @Override
	public IGenericBusiness<Calendar, Long> obtainBusinessInstance() {
		return calendarBusiness;
	}

    @RequestMapping()
    public String list() {
        return null;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/load")
    @ResponseBody
    public Collection<Map> ajaxLoad(Searchable searchable, @CurrentUser User loginUser) {
        Query condition = new Query();
        condition.addHibernateCondition("userId", loginUser.getId());
        List<Calendar> calendarList = (List<Calendar>) 
        		calendarBusiness.readDataListByCondition(condition, false);

        return Lists.<Calendar, Map>transform(calendarList, new Function<Calendar, Map>() {
            @SuppressWarnings("deprecation")
			@Override
            public Map apply(Calendar c) {
                Map<String, Object> m = Maps.newHashMap();

                Date startDate = new Date(c.getStartDate().getTime());
                Date endDate = org.platform.utils.date.DateUtils.obtainIntervalDate(startDate, c.getLength() - 1);
                boolean allDays = c.getStartTime() == null && c.getEndTime() == null;

                if(!allDays) {
                    startDate.setHours(c.getStartTime().getHours());
                    startDate.setMinutes(c.getStartTime().getMinutes());
                    startDate.setSeconds(c.getStartTime().getSeconds());
                    endDate.setHours(c.getEndTime().getHours());
                    endDate.setMinutes(c.getEndTime().getMinutes());
                    endDate.setSeconds(c.getEndTime().getSeconds());
                }

                m.put("id", c.getId());
                m.put("start", DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss"));
                m.put("end", DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss"));
                m.put("allDay", allDays);
                m.put("title", c.getTitle());
                m.put("details", c.getDetails());
                if(StringUtils.isNotEmpty(c.getBackgroundColor())) {
                    m.put("backgroundColor", c.getBackgroundColor());
                    m.put("borderColor", c.getBackgroundColor());
                }
                if(StringUtils.isNotEmpty(c.getTextColor())) {
                    m.put("textColor", c.getTextColor());
                }
                return m;
            }
        });
    }


    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewCalendar(@PathVariable("id") Calendar calendar, Model model) {
        model.addAttribute("calendar", calendar);
        return null;
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String showNewForm(
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = dataFormat) Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = dataFormat) Date end,
            Model model) {

        setColorList(model);

        Calendar calendar = new Calendar();
        calendar.setLength(1);
        if(start != null) {
            calendar.setStartDate(start);
            calendar.setLength((int)Math.ceil(1.0 * (end.getTime() - start.getTime()) / oneDayMillis));
            if(DateUtils.isSameDay(start, end)) {
                calendar.setLength(1);
            }
            if(!"00:00:00".equals(DateFormatUtils.format(start, "HH:mm:ss"))) {
                calendar.setStartTime(start);
            }
            if(!"00:00:00".equals(DateFormatUtils.format(end, "HH:mm:ss"))) {
                calendar.setEndTime(end);
            }

        }
        model.addAttribute("model", calendar);
        return null;
    }




    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public String newCalendar(@ModelAttribute("calendar") Calendar calendar, @CurrentUser User loginUser) {
        calendar.setUserId(loginUser.getId());
        calendarBusiness.insert(calendar);
        return "ok";
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    public String moveCalendar(
            @RequestParam("id") Long id,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = dataFormat) Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = dataFormat) Date end
    ) {
        Calendar calendar = (Calendar) calendarBusiness.readDataByPK(id, false);

        if(end == null) {
            end = start;
        }

        calendar.setStartDate(start);
        calendar.setLength((int)Math.ceil(1.0 * (end.getTime() - start.getTime()) / oneDayMillis));
        if(DateUtils.isSameDay(start, end)) {
            calendar.setLength(1);
        }
        if(!"00:00:00".equals(DateFormatUtils.format(start, "HH:mm:ss"))) {
            calendar.setStartTime(start);
        }
        if(!"00:00:00".equals(DateFormatUtils.format(end, "HH:mm:ss"))) {
            calendar.setEndTime(end);
        }
        calendarBusiness.update(calendar);

        return "ok";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteCalendar(@RequestParam("id") Long id) {
        calendarBusiness.deleteByPK(id);
        return "ok";
    }



    private void setColorList(Model model) {
        List<String> backgroundColorList = Lists.newArrayList();
        backgroundColorList.add("#3a87ad");
        backgroundColorList.add("#0d7813");
        backgroundColorList.add("#f2a640");
        backgroundColorList.add("#b373b3");
        backgroundColorList.add("#f2a640");
        backgroundColorList.add("#668cb3");
        backgroundColorList.add("#28754e");
        backgroundColorList.add("#8c66d9");

        model.addAttribute("backgroundColorList", backgroundColorList);
    }


}
