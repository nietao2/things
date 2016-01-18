package org.nietao.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class RemoteActor extends AbstractActor {

	public RemoteActor() {
			receive(ReceiveBuilder.match(Message.class, message -> {
				System.out.println(message.getMsg());
				sender().tell(new Message("Hello from the RemoteActor"), self());
			}).matchAny(o-> {
				System.out.println("RemoteActor got something unexpected.");
			}).build());
		}
	
}
