package org.platform.utils.message;

import org.platform.utils.spring.SpringUtils;
import org.springframework.context.MessageSource;

public class MessageUtils {

    private static MessageSource messageSource = null;

    /**
     * 根据消息键和参数 获取消息
     * 委托给spring messageSource
     * @param code 消息键
     * @param args 参数
     * @return
     */
    public static String getMessage(String code, Object... args) {
        if (null == messageSource) {
            messageSource = SpringUtils.getBean(MessageSource.class);
        }
        if (null == messageSource) {
        	messageSource = SpringUtils.getBean("messageSource");
        }
        return messageSource.getMessage(code, args, null);
    }

}
