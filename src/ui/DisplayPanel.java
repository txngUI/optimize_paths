package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import business.CitiesPath;
import business.City;
import input.CitiesManager;

public class DisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final DataRelatedUiValues dataForPrefRegion = new DataRelatedUiValues(285, 75, 575, 649);
	private final DataRelatedUiValues dataForPref = new DataRelatedUiValues(133, 73, 730, 700);
	private final DataRelatedUiValues data;

	public static DisplayPanel instance;

	private BufferedImage image;

	private CitiesPath path2display;
	
	private double[][] trails;

	public DisplayPanel() {
		instance = this;
		try {
			image = ImageIO.read(new File("images/france.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (CitiesManager.getInstance().getNbCities() > 12)
			data = dataForPref;
		else
			data = dataForPrefRegion;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
		
		if (trails != null) {
			paintTrail(g);
		}
		
		paintCities(g);

		if (path2display != null) {
			paintSolution(g);
		}
	}

	private void paintTrail(Graphics g) {
		g.setColor(new Color(0, 255, 0));
		
		for (int i = 0; i< CitiesManager.getInstance().getNbCities(); i++) {
			for (int j = i+1; j< CitiesManager.getInstance().getNbCities(); j++) {
				double value = trails[i][j];
				if (value >= 0.0005)
					showPheromoneBetween(g, i, j, value);
			}
		}
	}

	private void paintSolution(Graphics g) {
		g.setColor(new Color(255, 0, 0));
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(2));
		for (int i = 0; i < path2display.getNbCities() - 1; i++) {
			City v1 = path2display.getCityAt(i);

			City v2 = path2display.getCityAt(i + 1);
			g.drawLine(longitude2pixel(v1.getNormalizedCoordY()), latitude2pixel(v1.getNormalizedCoordX()),
					longitude2pixel(v2.getNormalizedCoordY()), latitude2pixel(v2.getNormalizedCoordX()));
		}
		g2.setStroke(new BasicStroke());
	}

	private void paintCities(Graphics g) {
		g.setColor(Color.BLACK);
		for (City city : CitiesManager.getInstance().getCities()) {
			int x = longitude2pixel(city.getNormalizedCoordY());
			int y = latitude2pixel(city.getNormalizedCoordX());
			g.fillOval(x, y, 5, 5);
			g.setFont(new Font("Arial", Font.PLAIN, 9));
			g.drawString(city.getName(), x, y);
		}
	}

	private int latitude2pixel(double coordX) {
		return (int) (coordX * data.ratioY) + data.dy;
	}

	private int longitude2pixel(double coordY) {
		return (int) (coordY * data.ratioX) + data.dx;
	}

	public void setPath2display(CitiesPath path2display) {
		this.path2display = path2display;
		repaint();
	}

	

	private void showPheromoneBetween(Graphics g, int city1index, int city2index, double value) {
		City v1 = CitiesManager.getInstance().getCity(city1index);
		City v2 = CitiesManager.getInstance().getCity(city2index);
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke((float) value*500));
       
		g2.draw(new Line2D.Float(longitude2pixel(v1.getNormalizedCoordY()), latitude2pixel(v1.getNormalizedCoordX()),
				longitude2pixel(v2.getNormalizedCoordY()), latitude2pixel(v2.getNormalizedCoordX())));
		g2.setStroke(new BasicStroke());
	}

	public void setTrails(double[][] trails2) {
		trails = trails2;
	}

}
