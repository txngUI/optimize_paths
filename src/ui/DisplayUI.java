package ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

import strategies.Soluce;

public class DisplayUI {
	
	private DisplayPanel mainPanel;
	private JLabel lengthLabel;
	private JLabel timeLabel;
	
	private Soluce soluce;

	public DisplayUI() {
		super();
		setupUI();
	}
	
	private void setupUI() {
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(new Dimension(1000, 925));
		
		mainPanel = new DisplayPanel();

		lengthLabel = new JLabel("0");
		timeLabel = new JLabel("");
		JMenuBar bar = new JMenuBar();
		bar.add(lengthLabel);
		bar.add(timeLabel);
		mainWindow.setJMenuBar(bar);

		mainWindow.setContentPane(mainPanel);
		mainWindow.setTitle("Probleme du voyageur de commerce");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
	
	public void setSoluce(Soluce aSoluce) {
		this.soluce = aSoluce;
	}

	public void refresh() {
		if (soluce.getPath() == null)
			return;
		lengthLabel.setText("Current soluce length = " + (int)soluce.getPath().getLength());
		
		double remainingTime = soluce.getDuration();
		int minutes = (int) (remainingTime/60000);
		remainingTime -= minutes*60000;   
		int secondes = (int) (remainingTime/1000);
		remainingTime -= secondes*1000;
		
		timeLabel.setText(" km & Duration = " 
				+ minutes + " min "
				+ secondes + " sec "
				+ remainingTime + " ms");
				
		mainPanel.setPath2display(soluce.getPath());
		mainPanel.repaint();
	}
	
	
	

}
