package TrafficJade;

import java.util.Random;

import Util.StaticValues;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.AgentTree.AgentNode;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ClientAgent extends Agent{
	
	int cars = 0;
	int clientNumber;
	IntersectionGui gui;
	AID superVisorAgent;
	ClientAgent myAgent;
	
	protected void setup() {
		this.clientNumber = Integer.valueOf(getArguments()[0].toString());
		myAgent = this;

		//initialize the UI frame
		gui = new IntersectionGui(this, clientNumber);
		gui.show();
		gui.setCounter(new Random().nextInt(10));
		
		//find the superVisor Agent
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(StaticValues.SUPERVISOR_TYPE);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length>0)
				superVisorAgent = result[0].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		System.out.println("as i agent : " + getName()+ "  i find superVisor: " + superVisorAgent.getName());
		
		
		addBehaviour(new RequestPerformer());
	}
	
	public void incrementCar(int car) {
		this.cars += car;
		gui.setCarCount(cars);
	}
	
	public void decrementCar(int car) {
		if (this.cars-car >= 0) {
			this.cars -=car;
		} else {
			this.cars = 0;
		}
		
		gui.setCarCount(this.cars);
	}
	
	
	protected void takeDown() {
		gui.dispose();
	}
	
	
	private class RequestPerformer extends Behaviour {
		
		private MessageTemplate mt; // The template to receive replies
		private int time = 0; // time gain from supervisor for next request
		private int step = 0;
		
		public void action() {
			switch (step) {
			case 0:
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				cfp.addReceiver(superVisorAgent);
				cfp.setContent(String.valueOf(cars));
				cfp.setLanguage(String.valueOf(clientNumber));
				cfp.setConversationId(StaticValues.SUPERVISOR_TYPE);
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // unique value
				myAgent.send(cfp);
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId(StaticValues.SUPERVISOR_TYPE),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;

			case 1:
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					time = Integer.valueOf(reply.getContent());
					gui.setWeight(Integer.valueOf(reply.getLanguage()));
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						gui.changeLight(StaticValues.GREEN_LIGHT_MODE);
						gui.setCounter(time);
					} else if (reply.getPerformative() == ACLMessage.REFUSE) {
						gui.changeLight(StaticValues.RED_LIGHT_MODE);
						gui.undefinedTime();
					}
					addBehaviour(new WakerBehaviour(myAgent, time*1000) {
						protected void handleElapsedTimeout() {
							addBehaviour(new RequestPerformer());
						}
					});
					step = 2;
				} else {
					block();
				}
				break;
			}
				
		}
		
		public boolean done() {
			return step==2;
		}
	}
	
}
