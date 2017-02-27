package org.platform.modules.rabbitmq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {
	
    public static void main( String[] args ) {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
    	FProducer fProducer = context.getBean(FProducer.class);
    	fProducer.sendMessage("this is first message");
    	fProducer.sendMessage("hello world");
    }
}
