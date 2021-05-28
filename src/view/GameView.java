package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.Toolkit;

public class GameView extends JFrame {
	private JPanel title;
	
	private JScrollPane logScrollBar;
	private JScrollPane logScrollBar2;
	private JScrollPane respondingScrollBar;
	private JScrollPane treatingScrollBar;
	
	private JPanel buttonPanel;
	
	private JPanel infoPanel;
	private JTextArea informationDisplay;
	
	private JPanel unitPanel;
	private JPanel availableUnits;
	private JTextArea respondingUnits;
	private JTextArea treatingUnits;
	
	private JPanel disasterPanel;
	private JTextArea currentDisasterDisplay;
	private JTextArea allStruckDisastersDisplay;
	
	private JButton cycle;
	private JTextField cycleIn;
	
	private JTextField casualties;
 
	
	
	public static void main(String[] args) {
		new GameView();
	}

	
	
	//Constructor
	public GameView() {
		
		title = new JPanel();
		add(title, BorderLayout.NORTH);
		
		infoPanel = new JPanel();
		add(infoPanel, BorderLayout.SOUTH);
		
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.CENTER);
		
		unitPanel = new JPanel();
		add(unitPanel, BorderLayout.EAST);
		
		disasterPanel = new JPanel();
		add(disasterPanel, BorderLayout.WEST);
		
		//Frame settings
		setSize(1000, 500);
		setTitle("Alien Invasion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//open in the middle of the screen
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPos = (dim.width/2) - (this.getWidth()/2);
		int yPos = (dim.height/2) - (this.getHeight()/2);
		this.setLocation(xPos,yPos);

		//Just the title and the current cycle in the NORTH of the FRAME
		
		JLabel titleLabel = new JLabel("Invaded City");
		cycle = new JButton("Next Cycle");
		cycleIn = new JTextField("Current Cycle" , 15);
		casualties = new JTextField("no. of casualties" ,15);
		
		title.add(titleLabel);
		title.add(cycle);
		title.add(cycleIn);
		title.add(casualties);
		
		
		
		//Map : Grid, in the CENTER of the FRAME
		
		buttonPanel.setLayout(new GridLayout(10, 10, 2, 2));
		

		//Unit Panel: contains: available units buttons; responding units; treating units; in the EAST of the FRAME
		unitPanel.setLayout(new GridLayout(3, 1, 4, 4));
		// (1) 
		availableUnits = new JPanel();
		availableUnits.setLayout(new GridLayout(0, 2, 2, 2));
		JLabel availableLabel = new JLabel("Available Units\n");
		availableUnits.add(availableLabel);
		unitPanel.add(availableUnits);
		
		// (2) 
		respondingUnits = new JTextArea(4,4);
		respondingUnits.setEditable(false);
		respondingUnits.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		respondingUnits.setText("respondingUnits\n ");
		respondingUnits.setLineWrap(true);
		respondingUnits.setWrapStyleWord(true);
		this.respondingScrollBar = new JScrollPane(respondingUnits, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		unitPanel.add(this.respondingScrollBar);
		

		//(3)
		treatingUnits = new JTextArea(4,4);
		treatingUnits.setEditable(false);
		treatingUnits.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		treatingUnits.setText("treatingUnits\n ");
		treatingUnits.setLineWrap(true);
		treatingUnits.setWrapStyleWord(true);
		this.treatingScrollBar = new JScrollPane(treatingUnits, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		unitPanel.add(this.treatingScrollBar);

		
		//Info Panel: displays target pressed in information textArea; in the SOUTH of the FRAME
		JLabel infoLabel = new JLabel("Information Panel");
		infoPanel.add(infoLabel);
		
		
		informationDisplay = new JTextArea(10,225);
		informationDisplay.setEditable(false);
		informationDisplay.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		informationDisplay.setLineWrap(true);
		informationDisplay.setWrapStyleWord(true);
		JScrollPane scrollbar1 = new JScrollPane(informationDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		infoPanel.add(scrollbar1);
		
		//Disaster Log:has 2 panels: currentDisasters, allStruckDisasters; in the WEST of the FRAME
		disasterPanel.setLayout(new GridLayout(2,1,4,4));

		//Active Disasters textArea
		currentDisasterDisplay = new JTextArea(5,55);
		currentDisasterDisplay.setEditable(false);
		currentDisasterDisplay.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		currentDisasterDisplay.setText("Active Disasters\n ");
		currentDisasterDisplay.setLineWrap(true);
		currentDisasterDisplay.setWrapStyleWord(true);
		logScrollBar = new JScrollPane(currentDisasterDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		disasterPanel.add(logScrollBar);
		
		
		//All struck Disasters textArea
		allStruckDisastersDisplay = new JTextArea(5,55);
		allStruckDisastersDisplay.setEditable(false);
		allStruckDisastersDisplay.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		allStruckDisastersDisplay.setText("All Disasters\n ");
		allStruckDisastersDisplay.setLineWrap(true);
		allStruckDisastersDisplay.setWrapStyleWord(true);
		logScrollBar2 = new JScrollPane(allStruckDisastersDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		disasterPanel.add(logScrollBar2);
		
		pack();
		setVisible(true);
		
	}
	public void addButton(JButton b) {
		buttonPanel.add(b);
	}
	
	public void addUnitButton(JButton b) {
		availableUnits.add(b);
	}
	
	public JTextArea getInformationDisplay() {
		return informationDisplay;
	}

	public JButton getCycle() {
		return cycle;
	}

	public void setButtonPanel(JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public void setInfoPanel(JPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	public void setInformationDisplay(JTextArea informationDisplay) {
		this.informationDisplay = informationDisplay;
	}


	public void setDisasterPanel(JPanel disasterPanel) {
		this.disasterPanel = disasterPanel;
	}

	public void setCurrentDisasterDisplay(JTextArea disasterDisplay) {
		this.currentDisasterDisplay = disasterDisplay;
	}

	public JTextArea getCurrentDisasterDisplay() {
		return currentDisasterDisplay;
	}

	public JPanel getAvailableUnits() {
		return availableUnits;
	}


	public void setAvailableUnits(JPanel availableUnits) {
		this.availableUnits = availableUnits;
	}


	public JTextArea getRespondingUnits() {
		return respondingUnits;
	}



	public void setRespondingUnits(JTextArea respondingUnits) {
		this.respondingUnits = respondingUnits;
	}



	public JTextArea getTreatingUnits() {
		return treatingUnits;
	}



	public void setTreatingUnits(JTextArea treatingUnits) {
		this.treatingUnits = treatingUnits;
	}



	public JTextArea getAllStruckDisastersDisplay() {
		return allStruckDisastersDisplay;
	}



	public void setAllStruckDisastersDisplay(JTextArea allStruckDisastersDisplay) {
		this.allStruckDisastersDisplay = allStruckDisastersDisplay;
	}



	public JPanel getDisasterPanel() {
		return disasterPanel;
	}



	public JTextField getCycleIn() {
		return cycleIn;
	}



	public void setCycleIn(JTextField cycleIn) {
		this.cycleIn = cycleIn;
	}



	public JTextField getCasualties() {
		return casualties;
	}



	public void setCasualties(JTextField casualties) {
		this.casualties = casualties;
	}

	
}
