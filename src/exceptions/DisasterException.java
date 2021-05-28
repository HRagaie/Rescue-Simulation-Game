package exceptions;

import model.disasters.Disaster;

abstract public class DisasterException extends SimulationException {
	Disaster disaster;

	public DisasterException() {
		// TODO Auto-generated constructor stub
	}

	public Disaster getDisaster() {
		return disaster;
	}
	
	public DisasterException(Disaster disaster){
		super();
		this.disaster = disaster;
	}
	
	public DisasterException(Disaster disaster,String message) {
		super(message);
		this.disaster = disaster;
	}

}
