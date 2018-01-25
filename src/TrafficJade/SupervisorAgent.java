package TrafficJade;
import java.util.ArrayList;
import java.util.List;

import Util.StaticValues;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SupervisorAgent extends Agent{
	
	int[] cars = new int[4];
	int[] weight = new int[4];
	int[] priority = new int[4];
	int lightChoosen;
	int time;
	int messageRecieveCounter = 0;
	List<ACLMessage> replyList = new ArrayList<>(4); 
	SupervisorAgent myAgent;
	
	protected void setup() {
		myAgent = this;
		DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(StaticValues.SUPERVISOR_TYPE);
        sd.setName("Calculate-TimeRemaining");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        
        addBehaviour(new CalculateTimeRequest());
	}
	
	private void calculateTime() {
		lightChoosen = 0;
		if (priority[lightChoosen] < priority[1])
			lightChoosen = 1;
		if (priority[lightChoosen] < priority[2])
			lightChoosen = 2;
		if (priority[lightChoosen] < priority[3])
			lightChoosen = 3;
		
		for (int i=0; i<4; i++) {
			if (i== lightChoosen || cars[i]==0)
				weight[i] = 1;
			else
				weight[i] +=1;
				
		}
		
		time = (int) (cars[lightChoosen]*StaticValues.CAR_TIME_NEED);
		if (time >10)
			time=10;
		if (time <1)
			time=1;
	}
	
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	private class CalculateTimeRequest extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				
				if (messageRecieveCounter==0)
					replyList = new ArrayList<>();
				messageRecieveCounter++;
				
				cars[messageRecieveCounter-1] = Integer.valueOf(msg.getContent());
				priority[messageRecieveCounter-1] = (int) (cars[messageRecieveCounter-1] * Math.pow(weight[messageRecieveCounter-1], 2));
				replyList.add(msg.createReply()); ///==========assume message receive in correct sequence, maybe this assume is wrong 
					
				if (messageRecieveCounter == 4) {
					messageRecieveCounter = 0;
					calculateTime();
					for(int i=0; i<replyList.size(); i++) {
						ACLMessage messageReply = replyList.get(i);
						if (i==lightChoosen)
							messageReply.setPerformative(ACLMessage.PROPOSE);
						else
							messageReply.setPerformative(ACLMessage.REFUSE);
						messageReply.setContent(String.valueOf(time));
						messageReply.setLanguage(String.valueOf(weight[i]));
						myAgent.send(messageReply);
					}
				}
			} else {
				block();
			}
		}
	}

}
