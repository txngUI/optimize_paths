package input;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.City;
import util.FlatEarthDist;

public class CitiesManager {

	private static CitiesManager instance = null;

	public static CitiesManager getInstance() {
		if (instance == null) {
			instance = new CitiesManager();
		}
		return instance;
	}

	private List<City> coordsVilles;

	private double minLongitude;
	private double maxLongitude;
	private double minLatitude;
	private double maxLatitude;

	private double[][] distanceBetween;

	private Map<City, Integer> city2index;

	public void loadCitiesFromFile(String fichier) {
		coordsVilles = new ArrayList<City>();

		minLongitude = 48;
		maxLongitude = 0;
		minLatitude = 0;
		maxLatitude = 0;

		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");

				// int distToCut = line.indexOf(",");
				// City aCity = new City(Double.parseDouble(line.substring(0, distToCut)),
				// Double.parseDouble(line.substring(distToCut + 1, line.length())));
				City aCity = new City(Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim()),
						parts[2]);
				coordsVilles.add(aCity);
				if (aCity.getCoordX() < minLongitude)
					minLongitude = aCity.getCoordX();
				if (aCity.getCoordX() > maxLongitude)
					maxLongitude = aCity.getCoordX();
				if (aCity.getCoordY() < minLatitude)
					minLatitude = aCity.getCoordY();
				if (aCity.getCoordY() > maxLatitude)
					maxLatitude = aCity.getCoordY();
			}

			br.close();

//			System.out.println("min longitude  = " + minLongitude);
//			System.out.println("max longitude  = " + maxLongitude);
//			System.out.println("min latitude  = " + minLatitude);
//			System.out.println("max latitude  = " + maxLatitude);
//			System.out.println("diff longitude  = " + (maxLongitude - minLongitude));
//			System.out.println("diff latitude  = " + (maxLatitude - minLatitude));

			City paris = coordsVilles.get(0);
			City strasbourg = coordsVilles.get(1);

			System.out.println(FlatEarthDist.distance(paris.getCoordY(), paris.getCoordX(), strasbourg.getCoordY(),
					strasbourg.getCoordX()));

			buildDistancesData();

			normalize();
		} catch (Exception e) {
			System.out.println(e.toString() + " : Erreur lors de l'ouverture du fichier des villes");
		}
		System.out.println(coordsVilles);
	}

	private void buildDistancesData() {
		int size = coordsVilles.size();
		distanceBetween = new double[size][size];
		city2index = new HashMap<>();

		for (int i = 0; i < size; i++) {
			distanceBetween[i][i] = 0;
			City city1 = coordsVilles.get(i);
			city2index.put(city1, i);
			for (int j = i+1; j < size; j++) {
				City city2 = coordsVilles.get(j);
				double distance = city1.calculateDistanceWith(city2);
				distanceBetween[i][j] = distance;
				distanceBetween[j][i] = distance;
			}
		}
	}

	private void normalize() {
		for (City ville : coordsVilles) {
			ville.setNormalizedCoordX(1 - (-minLongitude + ville.getCoordX()) / (maxLongitude - minLongitude));
			ville.setNormalizedCoordY((-minLatitude + ville.getCoordY()) / (maxLatitude - minLatitude));
		}

	}

	public City getCity(int index) {
		return coordsVilles.get(index);
	}

	public List<City> getCities() {
		List<City> cities = new ArrayList<>();
		for (City city : coordsVilles) {
			cities.add(city);
		}
		return cities;
	}

	public int getNbCities() {
		return coordsVilles.size();
	}

	public double distanceBetween(City city1, City city2) {
		return distanceBetween(city2index.get(city1),city2index.get(city2));
	}

	public double distanceBetween(Integer previousCityIndex, Integer cityIndex) {
		return distanceBetween[previousCityIndex][cityIndex];
	}

	public Integer city2Index(City city) {
		return city2index.get(city);
	}

	private List<Double> allEdges;

	public List<Double> getAllEdges() {

		if (allEdges == null) {
			allEdges = calculateAllEdges();
		}
		return allEdges;
	}

	private List<Double>  calculateAllEdges() {
		List<Double> res = new ArrayList<>();
		for (int i = 0; i < coordsVilles.size(); i++) {
			for (int j = 0; j < coordsVilles.size(); j++) {
				res.add(distanceBetween[i][j]);
			}
		}
		return res;
	}
}
