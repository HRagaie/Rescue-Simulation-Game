package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) { //??
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)
			jobsDone();

	}
	public void respond(Rescuable r) throws CannotTreatException,IncompatibleTargetException {
		//already has a target s, other than r == unit TREATING THEN set disaster to active again before switching
		if(!(r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this,r,"target must be a building");
		}
		
		if (((ResidentialBuilding) r).getFireDamage() == 0) {
			throw new CannotTreatException(this, r, "cannot treat Exception");

		}
			if (getTarget() != null && getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		
		}

	public String toString() {
		String s = "Unit's Type: Fire Truck" + super.toString();
		return s;
	}
}
