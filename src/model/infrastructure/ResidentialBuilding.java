package model.infrastructure;

import java.util.ArrayList;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Rescuable, Simulatable 
{

	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;
	private SOSListener emergencyService;
	
	public ResidentialBuilding(Address location) {
		this.location = location;
		this.structuralIntegrity=100;
		occupants= new ArrayList<Citizen>();
		//dnt need to set emergency service initially b null
	}
	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}
	public void setStructuralIntegrity(int structuralIntegrity) { //structural Integrity = 0, DECEASED
		this.structuralIntegrity = structuralIntegrity;
		if(structuralIntegrity<=0)
		{
			this.structuralIntegrity=0;
			for(int i = 0 ; i< occupants.size(); i++)
				occupants.get(i).setHp(0);
			this.getDisaster().setActive(false);
		}
	}
	public int getFireDamage() {
		return fireDamage;
	}
	public void setFireDamage(int fireDamage) {
		this.fireDamage = fireDamage;
		if(fireDamage<=0)
			this.fireDamage=0;
		else if(fireDamage>=100)
			this.fireDamage=100;
	}
	public int getGasLevel() {
		return gasLevel;
	}
	public void setGasLevel(int gasLevel) { //GAS LEVEL = 100, DECEASED
		this.gasLevel = gasLevel;
		if(this.gasLevel<=0)
			this.gasLevel=0;
		else if(this.gasLevel>=100)
		{
			this.gasLevel=100;
			for(int i = 0 ; i < occupants.size(); i++)
			{
				occupants.get(i).setHp(0);
			}
		}
	}
	public int getFoundationDamage() {
		return foundationDamage;
	}
	public void setFoundationDamage(int foundationDamage) { //FOUNDADTION DAMAGE =100 --> ST. INT =0 --> DECEASED
		this.foundationDamage = foundationDamage;
		if(this.foundationDamage>=100)
		{
			
			setStructuralIntegrity(0);
		}
			
	}
	public Address getLocation() {
		return location;
	}
	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}
	public Disaster getDisaster() {
		return disaster;
	}
	public void setEmergencyService(SOSListener emergency) {
		this.emergencyService = emergency;
	}
	public String toString() {
		String s = "Building Location: "+ location +"\n" + 
				"Building StructureIntegrity: "+ this.structuralIntegrity +"\n" +
				"Building Fire Damage: "+ this.fireDamage +"\n" +
				"Building Foundation Damage: "+ this.foundationDamage +"\n"+
				"Building Gas Level: "+ this.gasLevel +"\n" +
				"Building Occupant Size: "+ occupants.size() +"\n";
		for(int i =0; i<occupants.size();i++) {
			s += "Occupants:\n"+ "Citizen " +(i+1) + "\n" + occupants.get(i)+"\n";
		}
		return s;
						
	}
	@Override
	public void cycleStep() {
	
		if(foundationDamage>0)
		{
			
			int damage= (int)((Math.random()*6)+5); //5 to 10, cause a5er el range not included
			setStructuralIntegrity(structuralIntegrity-damage);
			
		}
		if(fireDamage>0 &&fireDamage<30)
			setStructuralIntegrity(structuralIntegrity-3);
		else if(fireDamage>=30 &&fireDamage<70)
			setStructuralIntegrity(structuralIntegrity-5);
		else if(fireDamage>=70)
			setStructuralIntegrity(structuralIntegrity-7);
		
	}
	
	@Override
	public void struckBy(Disaster d) {
		if(disaster!=null)
			disaster.setActive(false);
		disaster=d; //apply disaster to a building
		emergencyService.receiveSOSCall(this); //inform appropriate SOSListener
	}
}
