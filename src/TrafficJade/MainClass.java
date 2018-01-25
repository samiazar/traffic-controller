package TrafficJade;

import jade.wrapper.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class MainClass {

	public static void main (String args[]) throws InterruptedException, StaleProxyException {
		final Runtime runtime = Runtime.instance();
		runtime.setCloseVM(true);
		Profile mainProfile = new ProfileImpl(true);
		AgentContainer mainContainer = runtime.createMainContainer(mainProfile);
		AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
		rma.start();
		Thread.sleep(900);
		
        Profile anotherProfile;
        AgentContainer anotherContainer;
        AgentController agent;

        anotherProfile = new ProfileImpl(false);
        anotherContainer = runtime.createAgentContainer(anotherProfile);
        System.out.println("Starting up a Client1...");
        agent = anotherContainer.createNewAgent("client1", "ClientAgent", null);
        agent.start();
        Thread.sleep(900);
        System.out.println("Starting up a Client2...");
        agent = anotherContainer.createNewAgent("client2", "ClientAgent", null);
        agent.start();
        Thread.sleep(900);
        System.out.println("Starting up a Client3...");
        agent = anotherContainer.createNewAgent("client3", "ClientAgent", null);
        agent.start();
        Thread.sleep(900);
        System.out.println("Starting up a Client4...");
        agent = anotherContainer.createNewAgent("client4", "ClientAgent", null);
        agent.start();
        Thread.sleep(900);
	}
}
