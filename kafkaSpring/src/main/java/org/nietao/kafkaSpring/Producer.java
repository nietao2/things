package org.nietao.kafkaSpring;

import java.util.Random;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

public class Producer {
	private static final String CONFIG = "applicationContext.xml";
	private static Random rand = new Random();

	public static void main(String[] args) {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG);
		ctx.start();

		final MessageChannel channel = ctx.getBean("inputToKafka", MessageChannel.class);

		for (int i = 0; i < 100; i++) {
			channel.send(MessageBuilder.withPayload("Message-" + rand.nextInt()).setHeader("messageKey", String.valueOf(i)).setHeader("topic", "test").build());
		}

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ctx.close();
	}
}