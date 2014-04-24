package org.platform.modules.front.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.platform.modules.auth.biz.IAuthBusiness;
import org.platform.modules.auth.biz.IUserBusiness;
import org.platform.modules.auth.entity.Resource;
import org.platform.modules.auth.entity.User;
import org.platform.modules.front.annotation.CurrentUser;
import org.platform.modules.personal.biz.ICalendarBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminIndexController")
@RequestMapping("/admin")
public class IndexController {
	
	@javax.annotation.Resource(name = "authBusiness")
	private IAuthBusiness authBusiness = null;
	
	@javax.annotation.Resource(name = "userBusiness")
	private IUserBusiness userBusiness = null;
	
	@javax.annotation.Resource(name = "calendarBusiness")
	private ICalendarBusiness calendarBusiness = null;

    @RequestMapping(value = {"/{index:index;?.*}"}) 
    public String index(@CurrentUser User loginUser, Model model) {
    	String name = (String) SecurityUtils.getSubject().getPrincipal();
    	User user = userBusiness.readDataByName(name);
    	List<Resource> menus = authBusiness.readResourcesByTypeAndUserIdWithStrategyTwo(
    			Resource.TYPE_MENU, user.getId());
    	model.addAttribute("menus", menus);
        return "admin/index/index";
    }


    @RequestMapping(value = "/welcome")
    public String welcome(@CurrentUser User loginUser, Model model) {
    	if (null == loginUser) {
    		String name = (String) SecurityUtils.getSubject().getPrincipal();
    		loginUser = userBusiness.readDataByName(name);
    	}
    	//最近3天的日历
        model.addAttribute("calendarCount", calendarBusiness.countRecentlyCalendar(loginUser.getId(), 2));
        
        model.addAttribute("messageUnreadCount", 0);
        return "admin/index/welcome";
    }





}
