package controller;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import exceptions.*;
import model.disasters.Collapse;
import model.disasters.*;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.*;
import model.units.Unit;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulator;
import view.GameView;

public class CommandCenter implements SOSListener,ActionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Citizen> baseCitizens;
	private ArrayList<JButton> buttons;
	private GameView gameView;
	private JButton mapButton;
	private JButton unitButton;
	private ArrayList<JButton> unitButtonsArray;
	private Rescuable rescuable = null;
	private Unit unit = null;
	private ArrayList<Unit> respondingUnits;
	private ArrayList<Unit> treatingUnits;
	

	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		baseCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();//in old code kona bene3mel = newArrayList<Unit>!
		gameView = new GameView();
		buttons = new ArrayList<JButton>();
		unitButtonsArray = new ArrayList<JButton>();
		respondingUnits = new ArrayList<Unit>();
		treatingUnits = new ArrayList<Unit>();
		
		
		for(int i =0; i< emergencyUnits.size(); i++) {
			unitButton = new JButton();
			if(emergencyUnits.get(i) instanceof Ambulance)
				unitButton.setText("Ambulance");
			if(emergencyUnits.get(i) instanceof FireTruck)
				unitButton.setText("FireTruck");
			if(emergencyUnits.get(i) instanceof GasControlUnit)
				unitButton.setText("GasControlUnit");
			if(emergencyUnits.get(i) instanceof DiseaseControlUnit)
				unitButton.setText("DiseaseControlUnit");
			if(emergencyUnits.get(i) instanceof Evacuator)
				unitButton.setText("Evacuator");
			unitButton.setActionCommand(emergencyUnits.get(i).getUnitID());
			unitButton.addActionListener(this);
			unitButtonsArray.add(unitButton);
			gameView.addUnitButton(unitButton);
		}
		for (int i = 0; i < 100; i++) {
			mapButton = new JButton();
			mapButton.addActionListener(this);
			buttons.add(mapButton);
			gameView.addButton(mapButton);
			int x = i / 10;
			int y = i % 10;
			mapButton.setToolTipText("");
			this.checkMapLocation(x, y, mapButton);
		}
		gameView.getCycle().addActionListener(this);
		

		
	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		
		if (r instanceof ResidentialBuilding) {
			
			if (!visibleBuildings.contains(r)) {
				visibleBuildings.add((ResidentialBuilding) r);
				//currentlyActiveDisasters.add(((ResidentialBuilding) r).getDisaster());
			}
			
		} else {
			
			if (!visibleCitizens.contains(r)) {
				visibleCitizens.add((Citizen) r);
				//currentlyActiveDisasters.add(((Citizen) r).getDisaster());
			}
		}

	}
	
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			if ((JButton) source == gameView.getCycle()) {
				try {
					engine.nextCycle();
					displayIcons();
					
					gameView.getCasualties().setText( "" + (engine.calculateCasualties()) );
					
					String activeDisasters = "------------Currently Active Disaster----------\n" + "\n";
					String allStruckDisasters = "------------All Already Struck Disasters-----------\n" + "\n";
					String allDeadCitizens = "-------Dead Citizens and their Locations------\n" + "\n";
					for(int i =0; i< engine.getExecutedDisasters().size() ; i++) {
						allStruckDisasters += engine.getExecutedDisasters().get(i).toString() + "\n" + "-------------------------------\n";
						//System.out.println("iiiiiiiiiii = " + i  + "Checking active disasters -----------------\n" + engine.getExecutedDisasters().get(i).toString());

						if(engine.getExecutedDisasters().get(i).isActive() == true) {
							activeDisasters += engine.getExecutedDisasters().get(i).toString() + "\n" + "-------------------------------\n";
							//System.out.println("iiiiiiiiiii = " + i  + "Checking active disasters -----------------\n" + engine.getExecutedDisasters().get(i).toString());
						}
					}
					for(int i =0; i< engine.getCitizens().size(); i++) {
						if (engine.getCitizens().get(i).getState() == CitizenState.DECEASED) {
							allDeadCitizens += engine.getCitizens().get(i).toString() + "\n" + "-------------------------------\n";
						}
					}
					
					gameView.getRespondingUnits().setText("");
					gameView.getTreatingUnits().setText("");
					gameView.getCurrentDisasterDisplay().setText(activeDisasters);
					gameView.getAllStruckDisastersDisplay().setText(allStruckDisasters + "\n" + allDeadCitizens);
					
					if(engine.checkGameOver()) {
						String t = "Game Over and Score equal " + engine.calculateCasualties();
						JOptionPane.showMessageDialog(gameView,t,"info", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}
					

				} catch (CitizenAlreadyDeadException e1) {
					e1.getMessage();
					JOptionPane.showMessageDialog(gameView, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
				} catch (BuildingAlreadyCollapsedException e2) {
					e2.getMessage();
					JOptionPane.showMessageDialog(gameView, e2.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
				}
				
				
				((JButton)source).setActionCommand(engine.toString());
				gameView.getCycleIn().setText(((JButton) source).getActionCommand());

				gameView.getInformationDisplay().setText(((JButton) source).getActionCommand());
				for(int i =0; i<emergencyUnits.size();i++) {
					if(emergencyUnits.get(i).getState() == UnitState.IDLE) {
						this.unitButtonsArray.get(i).setVisible(true);
					}
					if(!(treatingUnits.contains(emergencyUnits.get(i))) && emergencyUnits.get(i).getState() == UnitState.TREATING) {
						this.treatingUnits.add(emergencyUnits.get(i));
						//gameView.getTreatingUnits().setText(gameView.getTreatingUnits().getText() + "\n" + emergencyUnits.get(i).toString());
					}
					if(!(respondingUnits.contains(emergencyUnits.get(i))) && emergencyUnits.get(i).getState() == UnitState.RESPONDING) {
						this.respondingUnits.add(emergencyUnits.get(i));
						//gameView.getRespondingUnits().setText(gameView.getRespondingUnits().getText()+"\n"+emergencyUnits.get(i).toString());
					}
					
				}
				for(int i =0; i<respondingUnits.size();i++) {
					gameView.getRespondingUnits().setText(gameView.getRespondingUnits().getText() + "\n" + respondingUnits.get(i).toString());
				}
				for(int i =0; i<treatingUnits.size();i++) {
					gameView.getTreatingUnits().setText(gameView.getTreatingUnits().getText() + "\n" + treatingUnits.get(i).toString() + "\n");
				}
				treatingUnits.clear();
				respondingUnits.clear();
			}
			
			if (buttons.contains((JButton) source)) {
				int i = buttons.indexOf((JButton) source);
				int x = i / 10;
				int y = i % 10;
				rescuable = checkMapLocation(x, y, (JButton) source);
				if (!(gameView.getInformationDisplay().getText().equals(((JButton) source).getActionCommand())))
					gameView.getInformationDisplay().setText(((JButton) source).getActionCommand());
			}
			
			if(unitButtonsArray.contains((JButton) source)) {
				if(rescuable != null){
					unit = emergencyUnits.get(unitButtonsArray.indexOf((JButton) source));

					try {
						
						unit.respond(rescuable);
						((JButton) source).setVisible(false);
						gameView.getRespondingUnits().setText(gameView.getRespondingUnits().getText() + "\n" + unit.toString());
						int x = rescuable.getLocation().getX();
						int y = rescuable.getLocation().getY();
						int z = Integer.parseInt(x + "" + y);
						
						buttons.get(z).setHorizontalTextPosition(JButton.CENTER);
						buttons.get(z).setVerticalTextPosition(JButton.CENTER);
						buttons.get(z).setText(((JButton) source).getText());
						
					} catch (CannotTreatException e1) {
						System.out.println(e1.getMessage());
						JOptionPane.showMessageDialog(gameView, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					}
					catch (IncompatibleTargetException a) {
						System.out.println(a.getMessage());
						JOptionPane.showMessageDialog(gameView, a.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}

	}
	}

	public Rescuable checkMapLocation(int x, int y, JButton button) {
		boolean check = false;
		boolean none = false;
		for (int i = 0; i < engine.getBuildings().size(); i++) {
			if (engine.getBuildings().get(i).getLocation().getX() == x && engine.getBuildings().get(i).getLocation().getY() == y) {
				check = true;
				none = true;
				String tooltip = "Here stands a building with Occupants equal: " + engine.getBuildings().get(i).getOccupants().size() + "\nOccupants: \n";

				for (int j = 0; j < engine.getBuildings().get(i).getOccupants().size(); j++) {
					tooltip += engine.getBuildings().get(i).getOccupants().get(j).getName();
				}
				button.setToolTipText(button.getToolTipText() + tooltip);
				if (engine.getBuildings().get(i).getDisaster() != null) {
					if (engine.getBuildings().get(i).getDisaster() instanceof Fire)
						button.setActionCommand("Disaster affecting the building is: Fire\n" + engine.getBuildings().get(i).toString());
					if (engine.getBuildings().get(i).getDisaster() instanceof Collapse) 
						button.setActionCommand("Disaster affecting the building is: collapse\n" + engine.getBuildings().get(i).toString());
					if (engine.getBuildings().get(i).getDisaster() instanceof GasLeak)
						button.setActionCommand("Disaster affecting the building is: Gas Leak\n" + engine.getBuildings().get(i).toString());
				}
				else {
					button.setActionCommand(engine.getBuildings().get(i).toString());
				}
				return engine.getBuildings().get(i);
			}
		}
		if (check == false) {
			for (int i = 0; i < engine.getCitizens().size(); i++) {
				if (engine.getCitizens().get(i).getLocation().getX() == x
						&& engine.getCitizens().get(i).getLocation().getY() == y) {
					none = true;
					button.setToolTipText(button.getToolTipText() + "Here stands Citizen\n");
					if (engine.getCitizens().get(i).getDisaster() != null) {
						if (engine.getCitizens().get(i).getDisaster() instanceof Infection)
							button.setActionCommand(engine.getCitizens().get(i).toString()
									+ "Disaster affecting the citizen is: Infection ");
						if (engine.getCitizens().get(i).getDisaster() instanceof Injury)
							button.setActionCommand(engine.getCitizens().get(i).toString()
									+ "Disaster affecting the citizen is: Injury ");
					}
					else {
						checkMoreThanOneCitizen(x,y,button);
					}
					return engine.getCitizens().get(i);
				}
			}
		}
		if (none == false) {
			button.setActionCommand("NOTHING HERE");
		}
		return null;
	}
	
	public void checkMoreThanOneCitizen(int x, int y, JButton b) {
		String s = "";
		for (int i = 0; i < engine.getCitizens().size(); i++) {
			if (engine.getCitizens().get(i).getLocation().getX() == x && engine.getCitizens().get(i).getLocation().getY() == y) {
				baseCitizens.add(engine.getCitizens().get(i));
				 s += "" + engine.getCitizens().get(i).toString() + "\n";
			}
			
		}
		b.setActionCommand(s);
	}
	
	
	
	public void displayIcons() {

		for (int i = 0; i < engine.getCitizens().size(); i++) {
			Citizen c = engine.getCitizens().get(i);
			int x = engine.getCitizens().get(i).getLocation().getX();
			int y = engine.getCitizens().get(i).getLocation().getY();
			int z = Integer.parseInt(x + "" + y);
			if(c.getState()!=CitizenState.DECEASED) {
				ImageIcon icon = new ImageIcon("citizen.jpg");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(65, 55, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				buttons.get(z).setIcon(icon);
			}

			if (c.getState() == CitizenState.DECEASED) {
				ImageIcon icon = new ImageIcon("dead.png");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(65, 55, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				buttons.get(z).setIcon(icon);;
			}

			if (c.getDisaster() != null) {
				buttons.get(z).setBackground(Color.red);
			}

		}
		for (int i = 0; i < engine.getBuildings().size(); i++) {
			ResidentialBuilding r = engine.getBuildings().get(i);

			int x = engine.getBuildings().get(i).getLocation().getX();
			int y = engine.getBuildings().get(i).getLocation().getY();
			int z = Integer.parseInt(x + "" + y);
			if (r.getOccupants().size() == 0) {
				ImageIcon icon = new ImageIcon("building.jpg");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(65, 55, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				buttons.get(z).setIcon(icon);
			} else {
				ImageIcon icon = new ImageIcon("buildingwithcitizen.png");
				Image img = icon.getImage();
				Image newimg = img.getScaledInstance(65, 55, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(newimg);
				buttons.get(z).setIcon(icon);
			}
			if (r.getDisaster() != null) {
				if (r.getDisaster() instanceof Collapse) {
					ImageIcon icon = new ImageIcon("collapse.png");
					Image img = icon.getImage();
					Image newimg = img.getScaledInstance(65, 55, java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
					buttons.get(z).setIcon(icon);
				}
				buttons.get(z).setBackground(Color.red);
			}

		}

	}
	
	
	public static void main(String[] args) throws Exception {
		CommandCenter c = new CommandCenter();
	}

}
