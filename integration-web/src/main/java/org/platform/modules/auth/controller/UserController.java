package org.platform.modules.auth.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.platform.entity.Query;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.controller.GenericController;
import org.platform.modules.auth.biz.IUserBusiness;
import org.platform.modules.auth.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/authority/user")
@SessionAttributes(value = "loginUser")
public class UserController extends GenericController<User, Long> {

	@Resource(name = "userBusiness")
	private IUserBusiness userBusiness = null;
	
	@Override
	public IGenericBusiness<User, Long> obtainBusinessInstance() {
		return userBusiness;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("user", new User());
		return defaultViewPrefix() + "/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Valid User user, BindingResult result, Model model) {
		Query condition = new Query();
		condition.addCondition("name", user.getName());
		condition.addCondition("password", user.getPassword());
		User u = (User) userBusiness.readDataByCondition(condition, false);
		if (result.hasErrors() || u == null) {
			return defaultViewPrefix() + "/login";
		}
		model.addAttribute("loginUser", u);
		return redirectToUrl("/authority/user");
	}

}
