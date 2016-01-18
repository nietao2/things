package org.nietao.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class HelloRemote {
	public static void main(String[] args) throws InterruptedException {
		Config config = ConfigFactory.load("application.conf");
		ActorSystem server = ActorSystem.create("HelloRemoteSystem", config
				.getConfig("server").withFallback(config));
		ActorRef remoteActor = server.actorOf(Props.create(RemoteActor.class),
				"RemoteActor");
		remoteActor.tell(new Message("The RemoteActor is alive"), ActorRef.noSender());
		
		ActorSystem client =  ActorSystem.create("LocalSystem", config
				.getConfig("client").withFallback(config));
		ActorRef clientActor = client.actorOf(Props.create(LocalActor.class), "LocalActor");
		clientActor.tell(new Start(), ActorRef.noSender());
		
		Thread.sleep(2000);
		server.shutdown();
		client.shutdown();
	}
}
