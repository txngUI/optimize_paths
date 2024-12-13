package business;

import java.util.List;

import input.CitiesManager;
import util.FlatEarthDist;

public class City implements Comparable<City> {

	private double coordX;
	private double coordY;
	private String name;
	
	private double normalizedCoordX;
	private double normalizedCoordY;
	

	public City(double coordX, double coordY, String name){
		this.coordX = coordX;
		this.coordY = coordY;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Ville [coordX=" + coordX + ", coordY=" + coordY + "]";
	}

	public double getCoordX() {
		return coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (this.coordX != other.coordX || this.coordY != other.coordY)
			return false;
		return true;
	}

	public double getNormalizedCoordX() {
		return normalizedCoordX;
	}

	public void setNormalizedCoordX(double normalizedCoordX) {
		this.normalizedCoordX = normalizedCoordX;
	}

	public double getNormalizedCoordY() {
		return normalizedCoordY;
	}

	public void setNormalizedCoordY(double normalizedCoordY) {
		this.normalizedCoordY = normalizedCoordY;
	}

	public String getName() {
		return name;
	}

	public City findClosestCityFrom(List<City> listCities) {
		double minDistance = 1000;
		City nearest = null;
		for (City city : listCities) {
			double distance = CitiesManager.getInstance().distanceBetween(this, city);
			if (distance < minDistance) {
				minDistance = distance;
				nearest = city;
			}
		}
		return nearest;
	}
	
	public double calculateDistanceWith(City city) {
		return FlatEarthDist.distance(getCoordY(), getCoordX(), city.getCoordY(), city.getCoordX());
	}

	@Override
	public int compareTo(City o) {
		return CitiesManager.getInstance().city2Index(this)
				.compareTo(CitiesManager.getInstance().city2Index(o)) ;
	}
	
	
}
