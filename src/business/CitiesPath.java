package business;

import java.util.ArrayList;
import java.util.List;

import input.CitiesManager;

public class CitiesPath {

	private List<City> cities;
	private double length;

	private City latestCity;

	public CitiesPath() {
		super();
		cities = new ArrayList<City>();
		length = 0.;
		latestCity = null;
	}

	public CitiesPath(CitiesPath citiesPath) {
		this();
		for (City city : citiesPath.getCities()) {
			addCity(city);
		}
	}

	public CitiesPath(List<City> cities) {
		this();
		for (City city : cities) {
			addCity(city);
		}
		addCity(cities.get(0));
	}

	public City getCityAt(int index) {
		return cities.get(index);
	}

	public void addCity(City city) {
		cities.add(city);

		if (latestCity != null) {
			length += CitiesManager.getInstance().distanceBetween(latestCity, city);
		}

		latestCity = city;
	}

	public double getLength() {
		return length;
	}

	public List<City> getCities() {
		return cities;
	}

	public int getNbCities() {
		return cities.size();
	}

	public void closeWithStartingCity() {
		addCity(cities.get(0));

	}

	public City getLastCity() {
		return cities.get(cities.size() - 1);
	}
}
