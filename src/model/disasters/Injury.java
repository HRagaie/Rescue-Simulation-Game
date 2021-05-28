package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;


public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
		super(startCycle, target);
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException
	{
		if (this.getTarget() instanceof Citizen) {
			if(((Citizen)this.getTarget()).getState()== CitizenState.DECEASED) {
				throw new CitizenAlreadyDeadException(this,"Citizen Dead");
			}
		}

		Citizen target = (Citizen)getTarget();
		target.setBloodLoss(target.getBloodLoss()+30);
		super.strike();
	}
	@Override
	public void cycleStep() {
		//System.out.println("INJURY CYCLE STEP ON: " + this.getTarget());
		Citizen target = (Citizen)getTarget();
		target.setBloodLoss(target.getBloodLoss()+10);
		
	}

	public String toString() {
		String s ="Disaster Type: Injury, " + super.toString();
		return s;
	}
}
