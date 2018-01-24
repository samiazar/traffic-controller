package TrafficJade;

import java.util.Random;

import jade.core.Agent;

public class ClientAgent extends Agent{
	
	int cars = 0;
	IntersectionGui gui; 
	
	protected void setup() {
		gui = new IntersectionGui(this, 1);
		gui.show();
		gui.setCounter(new Random().nextInt(10));
		
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
	
	
}
