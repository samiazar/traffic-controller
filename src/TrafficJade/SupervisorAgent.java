package TrafficJade;
import jade.core.Agent;

public class SupervisorAgent extends Agent{
	
	protected void setup() {
		System.out.println("Hello Intersection! I'm supervisor " + getAID().getName() +
				" and im ready!");
	}

}
