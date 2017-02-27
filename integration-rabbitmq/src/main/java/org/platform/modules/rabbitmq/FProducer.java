package org.platform.modules.rabbitmq;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class FProducer {

	@Resource(name="fAmqpTemplate")
    private AmqpTemplate amqpTemplate = null;

    public void sendMessage(Object message){
    	System.out.println("fproducer send message: " + message);
        amqpTemplate.convertAndSend("first", message);
    }
	
}
