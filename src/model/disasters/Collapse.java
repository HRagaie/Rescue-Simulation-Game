package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;


public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException 
	{
		if(this.getTarget() instanceof ResidentialBuilding) {
			if(((ResidentialBuilding)this.getTarget()).getStructuralIntegrity() == 0) {
				throw new BuildingAlreadyCollapsedException(this,"Building is Collapsed");
			}
		}

		ResidentialBuilding target= (ResidentialBuilding)getTarget();	
		target.setFoundationDamage(target.getFoundationDamage()+10); //set initially b 10
		super.strike();
	}
	public void cycleStep()
	{
		//System.out.println("COLLAPE CYCLE STEP ON: " + this.getTarget());
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFoundationDamage(target.getFoundationDamage()+10); //incremented by 10
	}

	public String toString() {
		String s ="Disaster Type: Collapse, " + super.toString();
		return s;
	}

}
