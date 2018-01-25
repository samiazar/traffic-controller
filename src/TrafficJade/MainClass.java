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
		Thread.sleep(100);
		
        Profile anotherProfile;
        AgentContainer anotherContainer;
        AgentController agent;

        anotherProfile = new ProfileImpl(false);
        anotherContainer = runtime.createAgentContainer(anotherProfile);
        //------run agents------
        agent = anotherContainer.createNewAgent("supervisor", "TrafficJade.SupervisorAgent", null);
        agent.start();
        Thread.sleep(100);
        
        System.out.println("Starting up a Client1...");
        Object[] args1 = new Object[1];
        args1[0] = "1";
        agent = anotherContainer.createNewAgent("client1", "TrafficJade.ClientAgent", args1);
        agent.start();
        Thread.sleep(100);
        System.out.println("Starting up a Client2...");
        Object[] args2 = new Object[1];
        args2[0] = "2";
        agent = anotherContainer.createNewAgent("client2", "TrafficJade.ClientAgent", args2);
        agent.start();
        Thread.sleep(100);
        System.out.println("Starting up a Client3...");
        Object[] args3 = new Object[1];
        args3[0] = "3";
        agent = anotherContainer.createNewAgent("client3", "TrafficJade.ClientAgent", args3);
        agent.start();
        Thread.sleep(100);
        System.out.println("Starting up a Client4...");
        Object[] args4 = new Object[1];
        args4[0] = "4";
        agent = anotherContainer.createNewAgent("client4", "TrafficJade.ClientAgent", args4);
        agent.start();
        Thread.sleep(100);
	}
}
