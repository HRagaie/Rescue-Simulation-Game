package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;


public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException
	{
		if(this.getTarget() instanceof ResidentialBuilding) {
			if(((ResidentialBuilding)this.getTarget()).getStructuralIntegrity() == 0) {
				throw new BuildingAlreadyCollapsedException(this,"Building is Collapsed");
			}
		}

		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFireDamage(target.getFireDamage()+10);
		super.strike();
	}

	@Override
	public void cycleStep() {
		//System.out.println("FIRE CYCLE STEP ON: " + this.getTarget());
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFireDamage(target.getFireDamage()+10);
		
	}

	public String toString() {
		String s ="Disaster Type: Fire, " + super.toString();
		return s;
	}
}
