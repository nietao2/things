package org.nietao.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;

public class LocalActor extends AbstractActor {
	
	ActorSelection remote = context().actorSelection("akka.tcp://HelloRemoteSystem@127.0.0.1:5150/user/RemoteActor");
	int count = 0;

	public LocalActor() {
		receive(ReceiveBuilder
				.match(Start.class, start ->remote.tell(new Message("Hello from the LocalActor"), self()))
				.match(Message.class, message -> {
					System.out.println("LocalActor received message: " + message.getMsg());
				})
				.build());
	}
}