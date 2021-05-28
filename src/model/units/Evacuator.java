package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0 || target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity() && i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX() + target.getLocation().getY());

	}
	public void respond(Rescuable r) throws CannotTreatException,IncompatibleTargetException {
		//already has a target s, other than r == unit TREATING THEN set disaster to active again before switching
		if(!(r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this,r,"must be a bulding");
		}
		
		if (((ResidentialBuilding) r).getStructuralIntegrity() == 0 || !(((ResidentialBuilding) r).getDisaster() instanceof Collapse) )  {
			throw new CannotTreatException(this, r, "cannot treat Exception");

		}
			if (getTarget() != null && getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		
		}

	public String toString() {
		String s = "Unit's Type: Evacuator" + ", Maximum Capacity: " + this.getMaxCapacity() + ", No. of Occupants: " + this.getPassengers().size();
		for(int i =0; i<this.getPassengers().size();i++) {
			s += "Passengers:\n"+ "Citizen " +(i+1) + "\n" + this.getPassengers().get(i)+"\n";
		}
		s += super.toString();
		return s;
	}
}
