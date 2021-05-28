package model.units;

import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;

public abstract class MedicalUnit extends Unit {
	private int healingAmount;
	private int treatmentAmount;
	public MedicalUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {
		super(unitID, location, stepsPerCycle,worldListener);
		healingAmount=10;
		treatmentAmount=10;
	}

	public int getTreatmentAmount() {
		return treatmentAmount;
	}
	public  void heal() 
	{ //old code awel lama ba5osh fel heal I make unit b idle & I set citizen rescued bs howa msh 3ayez keda?
		Citizen target = (Citizen)getTarget();
		if(target.getHp()<100)
			target.setHp(target.getHp()+healingAmount);
		
		
		if(target.getHp() == 100)	
			jobsDone();
		
	}
	
	
}
