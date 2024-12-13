package strategies.heuristics.ant;

import java.util.HashSet;
import java.util.Set;

import business.CitiesPath;
import business.City;
import input.CitiesManager;

public class Ant {

	private CitiesPath visitedCities;
	private Set<City> remainingCities;
	private City randomStartingCity;

	public Ant(int nbCities) {
		randomStartingCity = CitiesManager.getInstance().getCity((int) (Math.random() * nbCities));
		clearMemory();
	}

	public void clearMemory() {
		visitedCities = new CitiesPath();
		remainingCities = new HashSet<City>(CitiesManager.getInstance().getCities());
		visitedCities.addCity(randomStartingCity);
		remainingCities.remove(randomStartingCity);
	}

	public CitiesPath getVisitedCities() {
		return visitedCities;
	}

	public void moveNextCity() {

		double[] probabilities = calculateProbabilities();
		
		double randomValue = Math.random();
		double total = 0;
		
		for (int i = 0; i < AntColonyStrategy.nbCities; i++) {
			total += probabilities[i];
		}
		//System.out.println("Total = " + total);
		total = 0;
		
		City chosenCity = null;
		for (int i = 0; i < AntColonyStrategy.nbCities; i++) {
			total += probabilities[i];
			if (total >= randomValue || Double.isNaN(total)) {
				chosenCity = CitiesManager.getInstance().getCity(i);
				break;
			}
		}

		if (!remainingCities.contains(chosenCity)) {
			System.err.println("choix de ville incensé");
			System.exit(1);
		}

		visitedCities.addCity(chosenCity);
		remainingCities.remove(chosenCity);
		
		if (remainingCities.isEmpty()) {
			visitedCities.addCity(visitedCities.getCities().get(0));
		}
	}

	private double[] calculateProbabilities() {
		double[] result = new double[AntColonyStrategy.nbCities];

		City lastCity = getLastVisitedCity();

		for (int i = 0; i < AntColonyStrategy.nbCities; i++) {
			result[i] = 0.;
		}
		
		double pheromoneTotal = 0.0;
		for (City remainingCity : remainingCities) {
			int indexFirstCity = CitiesManager.getInstance().city2Index(lastCity);
			int indexSecondCity = CitiesManager.getInstance().city2Index(remainingCity);
			double localPheromone = AntColonyStrategy.trails[indexFirstCity][indexSecondCity];
			double distance = CitiesManager.getInstance().distanceBetween(lastCity, remainingCity);

			double pheromone = Math.pow(localPheromone, AntColonyStrategy.ALPHA)
					* Math.pow(1.0 / distance, AntColonyStrategy.BETA);
			pheromoneTotal += pheromone;
			result[CitiesManager.getInstance().city2Index(remainingCity)] = pheromone;
		}

		double numerator = 0;
		for (City futureCity : remainingCities) {
			numerator = result[CitiesManager.getInstance().city2Index(futureCity)];
			result[CitiesManager.getInstance().city2Index(futureCity)] = numerator / pheromoneTotal;
		}

		return result;
	}

	private City getLastVisitedCity() {
		return visitedCities.getCities().get(visitedCities.getCities().size() - 1);
	}

	public boolean hasCompletePath() {
		return remainingCities.isEmpty();
	}

	public double getTrailLength() {
		return visitedCities.getLength();
	}

}
