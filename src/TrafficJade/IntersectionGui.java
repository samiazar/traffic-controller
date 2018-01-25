package TrafficJade;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Util.StaticValues;

public class IntersectionGui extends JFrame implements ActionListener{
	
	private ClientAgent agent;
	private JLabel lAgentName;
	private JLabel lCounter;
	private JLabel lCarsCount;
	private JLabel lWeight;
	private int agentNumber;
	private Timer timer;
	private boolean isTimerRun;
	private int second;
	
	public IntersectionGui(ClientAgent agent, int agentNumber) {
		super("Traffic lightening Controler");
		this.agent = agent;
		this.agentNumber = agentNumber;
		setSize(300, 250);
		setResizable(false);
		setFramePosition();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(agent.getName());
		
		setLayout(new GridLayout(4,1));
		lAgentName = new JLabel("Agent #" + agentNumber);
		lAgentName.setHorizontalAlignment(JLabel.CENTER); 
		lAgentName.setVerticalAlignment(JLabel.CENTER);
		add(lAgentName);
		lCounter = new JLabel();
		lCounter.setText("--:--");
		lCounter.setHorizontalAlignment(JLabel.CENTER); 
		lCounter.setVerticalAlignment(JLabel.CENTER);
		add(lCounter);
		
		Container infoContainer = new Container();
		infoContainer.setLayout(new GridLayout(1,2));
		lCarsCount = new JLabel("0 cars");
		lCarsCount.setHorizontalAlignment(JLabel.CENTER); 
		lCarsCount.setVerticalAlignment(JLabel.CENTER);
		infoContainer.add(lCarsCount);
		lWeight = new JLabel("weight: " + 1);
		lWeight.setHorizontalAlignment(JLabel.CENTER); 
		lWeight.setVerticalAlignment(JLabel.CENTER);
		infoContainer.add(lWeight);
		add(infoContainer);
		changeLight(StaticValues.RED_LIGHT_MODE);
		
				
		Container buttonContainer = new Container();
		buttonContainer.setLayout(new GridLayout(1,4));
		JButton button1 = new JButton("+1car");
		button1.addActionListener(this);
		button1.setActionCommand("1car");
		buttonContainer.add(button1);
		JButton button2 = new JButton("+3car");
		button2.addActionListener(this);
		button2.setActionCommand("3car");
		buttonContainer.add(button2);
		JButton button3 = new JButton("+5car");
		button3.addActionListener(this);
		button3.setActionCommand("5car");
		buttonContainer.add(button3);
		JButton button4 = new JButton("+7car");
		button4.addActionListener(this);
		button4.setActionCommand("7car");
		buttonContainer.add(button4);
		add(buttonContainer);
		
	}
	
	private void setFramePosition() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		switch(agentNumber) {
			case 1:
				this.setLocation(dim.width/2-(3*this.getSize().width/2), dim.height/2-this.getSize().height/2);
				break;
			case 2:
				this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-(3*this.getSize().height/2));
				break;
			case 3:
				this.setLocation(dim.width/2+(this.getSize().width/2), dim.height/2-this.getSize().height/2);
				break;
			case 4:
				this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2);
				break;
			
		}

	}

	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {

		switch (actionEvent.getActionCommand()) {
		case "1car":
			agent.incrementCar(1);
			break;
		case "3car":
			agent.incrementCar(3);
			break;
		case "5car":
			agent.incrementCar(5);
			break;
		case "7car":
			agent.incrementCar(7);
			break;
		}
	}

	public void setCarCount(int carCount){
		lCarsCount.setText(carCount + " cars");
	}
	
	public void setWeight(int weight) {
		lWeight.setText("weight: " + weight);
	}


	public void setCounter(int seconds) {
		this.second = seconds;
		if (!isTimerRun) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					second -= 1;
					isTimerRun = true;
					agent.decrementCar(2);
					counterSetText();
					if (second <1) {
						timer.cancel();
						isTimerRun = false;
					}
				}
			}, 0, 1000);
		}
	}
	
	public void undefinedTime() {
		second = -1;
		counterSetText();
	}
			
	
	private void counterSetText(){
		if (second > 10 )
			lCounter.setText("00:"+second);
		else if (second>=0)
			lCounter.setText("00:0"+second);
		else
			lCounter.setText("--:--");
		}

	public void changeLight(int lightMode) {
		ImageIcon trafficLight = new ImageIcon("res/red.png");;
		if (lightMode == StaticValues.RED_LIGHT_MODE) {
			trafficLight = new ImageIcon("res/red.png");
		} else if (lightMode == StaticValues.GREEN_LIGHT_MODE) {
			trafficLight = new ImageIcon("res/green.png");
		}
	    BufferedImage resizedImg = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(trafficLight.getImage(), 0, 0, 30, 30, null);
	    g2.dispose();
	    
	    trafficLight.setImage(resizedImg);
	    lCounter.setIcon(trafficLight);
	}


}
