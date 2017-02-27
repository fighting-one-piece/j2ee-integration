package org.platform.modules.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import org.springframework.amqp.core.MessageProperties;

@Component("fConsumer")
public class FConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("fconsumer receive message: " + message);
		System.out.println("fconsumer receive message: " + new String(message.getBody()));
		MessageProperties properties = message.getMessageProperties();
		System.out.println("fconsumer receive message property: " + properties.getClusterId());
		System.out.println("fconsumer receive message property: " + properties.getConsumerQueue());
		System.out.println("fconsumer receive message property: " + properties.getConsumerTag());
		System.out.println("fconsumer receive message property: " + properties.getContentEncoding());
		System.out.println("fconsumer receive message property: " + properties.getContentLength());
		System.out.println("fconsumer receive message property: " + properties.getContentType());
		System.out.println("fconsumer receive message property: " + properties.getMessageId());
		System.out.println("fconsumer receive message property: " + properties.getReceivedExchange());
		System.out.println("fconsumer receive message property: " + properties.getReceivedRoutingKey());
		System.out.println("fconsumer receive message property: " + properties.getReceivedUserId());
		System.out.println("fconsumer receive message property: " + properties.getReceivedDelay());
		System.out.println("fconsumer receive message property: " + properties.getReceivedDeliveryMode());
		System.out.println("fconsumer receive message property: " + properties.getExpiration());
		System.out.println("fconsumer receive message property: " + properties.getTimestamp());
		System.out.println("fconsumer receive message property: " + properties.getHeaders());
		
	}
	
}
