package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;


public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {
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
	target.setToxicity(target.getToxicity()+25);
	super.strike();
}
	@Override
	public void cycleStep() {
		//System.out.println("INFECTION CYCLE STEP ON: " + this.getTarget());
		Citizen target = (Citizen)getTarget();
		target.setToxicity(target.getToxicity()+15);
		
	}

	public String toString() {
		String s ="Disaster Type: Infection, " + super.toString();
		return s;
	}
}
