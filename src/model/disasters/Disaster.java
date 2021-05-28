package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active; //default false
	
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	} 
	public String toString() {
		String s = "Start Cycle: " + this.startCycle + " Target: " + this.target + "isActive: " + this.active;
		return s;
	}
	public void strike() throws CitizenAlreadyDeadException , BuildingAlreadyCollapsedException
	{
		target.struckBy(this);
		active=true;
	}
}
