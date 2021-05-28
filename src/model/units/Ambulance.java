package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) { //if dead
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) { //reduce by treatment amount
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0) //batal y bleed --> RESCUED
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0) //nxt cycle heal
			heal();

	}

	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException { // medical unit in
																								// healing phase, then
																								// dnt reactivate
																								// disaster
			if(!(r instanceof Citizen)) {
				throw new IncompatibleTargetException(this,r,"must be citizen");
			}
			
			if (((Citizen) r).getBloodLoss() == 0) {
				throw new CannotTreatException(this, r, "cannot treat Exception");

			}
				if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0 && getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			
		} 

	public String toString() {
		String s = "Unit's Type: Ambulance" + super.toString();
		return s;
	}
		

}
