package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;


public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
	}
	
	@Override
	public void strike() throws BuildingAlreadyCollapsedException, CitizenAlreadyDeadException
	{
		if(this.getTarget() instanceof ResidentialBuilding) {
			if(((ResidentialBuilding)this.getTarget()).getStructuralIntegrity() == 0) {
				throw new BuildingAlreadyCollapsedException(this,"Building is Collapsed");
			}
		}
		

		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setGasLevel(target.getGasLevel()+10);
		super.strike();
	}
	@Override
	public void cycleStep() {
		//System.out.println("GAS LEAK CYCLE STEP ON: " + this.getTarget());
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setGasLevel(target.getGasLevel()+15);
		
	}

	public String toString() {
		String s ="Disaster Type: Gas Leak, " + super.toString();
		return s;
	}
}
