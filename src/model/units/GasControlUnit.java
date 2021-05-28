package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) { //shaleit a failure mn old code 
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0) 
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}
	public void respond(Rescuable r) throws CannotTreatException,IncompatibleTargetException {
		//already has a target s, other than r == unit TREATING THEN set disaster to active again before switching
		if(!(r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this,r,"must be citizen");
		}
		
		if (((ResidentialBuilding) r).getGasLevel() == 0) {
			throw new CannotTreatException(this, r, "cannot treat Exception");

		}
			if (getTarget() != null && getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		
		}

	public String toString() {
		String s = "Unit's Type: Gas Control Unit" + super.toString();
		return s;
	}
}
