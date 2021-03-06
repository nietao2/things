package org.nietao.kafkaSpring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;


public class Consumer {
	private static final String CONFIG = "applicationContext.xml";
	private static Random rand = new Random();

	@SuppressWarnings({ "unchecked", "unchecked", "rawtypes" })
	public static void main(String[] args) {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG);
		ctx.start();

		final QueueChannel channel = ctx.getBean("inputFromKafka", QueueChannel.class);
		Message msg;		
		while((msg = channel.receive()) != null) {
			HashMap map = (HashMap)msg.getPayload();
			Set<Map.Entry> set = map.entrySet();
			for (Map.Entry entry : set) {
				String topic = (String)entry.getKey();
				System.out.println("Topic:" + topic);
				ConcurrentHashMap<Integer,List<String>> messages = (ConcurrentHashMap<Integer,List<String>>)entry.getValue();
				Collection<List<String>> values = messages.values();
				for (Iterator<List<String>> iterator = values.iterator(); iterator.hasNext();) {
					List<String> list = iterator.next();
					for (String object : list) {
						String message = new String(object);
						System.out.println("\tMessage: " + message);
					}
					
				}
			
			}
			
		}
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ctx.close();
	}
}
