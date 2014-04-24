package org.platform.modules.front.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.platform.modules.auth.shiro.jcaptcha.JCaptcha;
import org.platform.utils.web.response.ValidateResponse;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * jcaptcha 验证码验证
 */
@Controller
@RequestMapping("/jcaptcha-validate")
public class AjaxJCaptchaValidateController {
	
	@Resource(name = "messageSource")
	private MessageSource messageSource = null;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object jqueryValidationEngineValidate(
            HttpServletRequest request,
            @RequestParam(value = "fieldId", required = false) String fieldId,
            @RequestParam(value = "fieldValue", required = false) String fieldValue) {

        ValidateResponse response = ValidateResponse.newInstance();

        if (JCaptcha.hasCaptcha(request, fieldValue) == false) {
            response.validateFail(fieldId, messageSource.getMessage("jcaptcha.validate.error", null, null));
        } else {
            response.validateSuccess(fieldId, messageSource.getMessage("jcaptcha.validate.success", null, null));
        }
        return response.result();
    }
}
