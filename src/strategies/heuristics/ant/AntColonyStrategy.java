package strategies.heuristics.ant;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import ui.DisplayPanel;

public class AntColonyStrategy extends PathStategy {

	// parameters
	private static final int ANTS_FACTOR = 10;
	private static final int MAX_ITERATIONS = 1000;
	private static double C = 0.0001;
	public static final float ALPHA = 15f;
	public static final float BETA = 5f;
	private static final float EVAPORATION_RATE = 0.15f;
	private static double Q = 0.2;

	private static int MAX_ITERATIONS_WITHOUT_BETTER_PATH = 100;

	private Set<Ant> ants;
	public static int nbCities;
	private int nbAnts;
	private CitiesPath bestTourOrder;
	private double bestTourLength;
	private int nbIterationsWithoutBetterPath = 0;

	public static double trails[][];

	@Override
	protected CitiesPath runStrategy() {

		initializeColony();
		initializePheromons();

		int prct = 0;
		for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
			prct = refreshDisplay(prct, iteration);
			moveAnts();
			evaporatePheromones();
			updatePheromones();
			checkBestPath();
			clearAntsMemory();
			if (terminationCriterionMet()) {
				System.out.println("Arret en avance");
				break;
			}
		}

		return bestTourOrder;
	}

	private int refreshDisplay(int prct, int iteration) {
		if (iteration % (MAX_ITERATIONS/10) == 0) {
			prct += 10;
			System.out.println("" + prct + "%");
		}
		if (iteration % (MAX_ITERATIONS/100) == 0) {
			DisplayPanel.instance.repaint();
		}
		return prct;
	}

	private void clearAntsMemory() {
		for (Ant ant : ants) {
			ant.clearMemory();
		}
	}

	private void moveAnts() {
		for (int i = 0; i < nbCities - 1; i++) {
			ants.forEach(ant -> ant.moveNextCity());
		}
		for (Ant ant : ants) {
			if (!ant.hasCompletePath()) {
				System.err.println("parcours non complet alors que ca devrait");
				System.exit(1);
			}
		}
	}

	private void checkBestPath() {
		nbIterationsWithoutBetterPath++;
		if (bestTourOrder == null) {
			Ant ant = ants.iterator().next();
			bestTourOrder = ant.getVisitedCities();
			bestTourLength = ant.getTrailLength();
			DisplayPanel.instance.setTrails(trails);
			DisplayPanel.instance.setPath2display(bestTourOrder);
			
		}
		for (Ant ant : ants) {
			if (ant.getTrailLength() < bestTourLength) {
				bestTourLength = ant.getTrailLength();
				bestTourOrder = new CitiesPath(ant.getVisitedCities().getCities());
				DisplayPanel.instance.setPath2display(bestTourOrder);
				nbIterationsWithoutBetterPath = 0;
				System.out.println("New best = " + bestTourLength);
			}
		}
	}

	private boolean terminationCriterionMet() {
		return nbIterationsWithoutBetterPath >= MAX_ITERATIONS_WITHOUT_BETTER_PATH;
	}

	private void evaporatePheromones() {
		for (int i = 0; i < nbCities; i++) {
			for (int j = 0; j < nbCities; j++) {
				trails[i][j] *= 1 - EVAPORATION_RATE;
			}
		}
	}

	private void updatePheromones() {
		for (Ant ant : ants) {
			double contribution = Q / ant.getTrailLength();

			City previousCity = null;
			for (City currentCity : ant.getVisitedCities().getCities()) {
				if (previousCity != null) {
					int index1 = CitiesManager.getInstance().getCities().indexOf(previousCity);
					int index2 = CitiesManager.getInstance().getCities().indexOf(currentCity);
					trails[index1][index2] += contribution;
				}
				previousCity = currentCity;
			}
			if (ant.getVisitedCities().getCities().size() > 1) {
				int indexLast = CitiesManager.getInstance().getCities().indexOf(previousCity);
				int indexFirst = CitiesManager.getInstance().getCities().indexOf(ant.getVisitedCities().getCities().get(0));
				trails[indexLast][indexFirst] += contribution;
			}
		}
	}

	private void initializePheromons() {
		trails = new double[nbCities][nbCities];
		IntStream.range(0, nbCities).forEach(i -> {
			IntStream.range(0, nbCities).forEach(j -> trails[i][j] = C);
		});
	}

	private void initializeColony() {
		nbCities = CitiesManager.getInstance().getCities().size();
		nbAnts = ANTS_FACTOR * nbCities;
		ants = new HashSet<Ant>();

		IntStream.range(0, nbAnts).forEach(i -> ants.add(new Ant(nbCities)));

	}

	public double[][] getTrails() {
		return trails;
	}

}
