package org.nietao.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ActorDemo {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		ActorRef ref = system.actorOf(Props.create(HelloActor.class));
		ref.tell("World", ActorRef.noSender());
		system.shutdown();
	}

}

class HelloActor extends UntypedActor{

	public void onReceive(Object arg0) throws Exception {
		System.out.println("Hello, " + arg0);
	}
	
}