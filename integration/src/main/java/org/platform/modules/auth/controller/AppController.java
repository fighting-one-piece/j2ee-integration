package org.platform.modules.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/app")
public class AppController {

	@ResponseBody
	@RequestMapping(value = "/test")
	public String test() {
		return "app test";
	}
	
}
