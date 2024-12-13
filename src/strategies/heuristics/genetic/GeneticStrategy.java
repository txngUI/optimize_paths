package strategies.heuristics.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import strategies.SoluceType;
import strategies.StrategyManager;
import ui.DisplayPanel;

public class GeneticStrategy extends PathStategy {

	private static final int MAX_ITERATIONS = 100000;
	private static final int POPULATION_SIZE = 300;
	private static final int REPRODUCTION_SIZE = 10;
	private static final float MUTATION_RATE = 0.6f;

	private Population population;
	private int genomeSize = CitiesManager.getInstance().getNbCities();

	private int prctProgress = 0;
	private Individual best;

	@Override
	protected CitiesPath runStrategy() {

		initPopulationWithNN();
		//initRandomPopulation();

		best = population.getFirstIndividual();

		for (int i = 0; i < MAX_ITERATIONS; i++) {
			displayMilestones(i);
			List<Individual> selected = selectBestIndividuals();

			population = createNewGeneration(selected);

			updateBestIndividual();
		}
		return best.getGenome();
	}

	private Individual updateBestIndividual() {
		Individual bestFromIteration = Collections.min(population.getIndividuals());
		if (bestFromIteration.getLength() < best.getLength()) {
			best = bestFromIteration;
			System.out.println("Best length = " + best.getLength());
			DisplayPanel.instance.setPath2display(best.getGenome());
		}
		return best;
	}

	private void displayMilestones(int i) {
		if (i % (MAX_ITERATIONS / 10) == 0) {
			prctProgress +=10;
			System.out.println("" + prctProgress + "%");
		}
	}

	private void initRandomPopulation() {
		population = new Population();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.addIndividual(new Individual());
		}
	}

	private void initPopulationWithNN() {
		population = new Population();
		CitiesPath citiesNN = StrategyManager.createSolucePath(SoluceType.NN).getPath();


		for (int i = 0; i < POPULATION_SIZE; i++) {
			Individual aNewIndividual = mutate(new Individual(citiesNN));
			population.addIndividual(aNewIndividual);
		}
	}

	private Population createNewGeneration(List<Individual> selected) {
		Population generation = new Population();
		//generation.addSeveralIndividuals(selected);
		//currentGenerationSize = generation.getSize();
		while (generation.getSize() < POPULATION_SIZE) {
			List<Individual> parents = pickNRandomElements(selected, 2);
			List<Individual> children = crossover(parents);
			children.set(0, mutate(children.get(0)));
			children.set(1, mutate(children.get(1)));
			generation.addSeveralIndividuals(children);
//			currentGenerationSize += 2;
		}
		return generation;
	}

	public List<Individual> pickNRandomElements(List<Individual> list, int nb) {
		List<Individual> result = new ArrayList<Individual>();

		Random r = new Random();
		for (int i = 0; i < nb; i++) {
			result.add(list.get(r.nextInt(list.size())));
		}
		return result;
	}

	public List<Individual> crossover(List<Individual> parents) {

		Random random = new Random();
		int breakpoint = random.nextInt(genomeSize);
		List<Individual> children = new ArrayList<>();

		// copy parental genomes - we copy so we wouldn't modify in case they were
		// chosen to participate in crossover multiple times

		CitiesPath parent1Genome = new CitiesPath(parents.get(0).getGenome().getCities());
		CitiesPath parent2Genome = new CitiesPath(parents.get(1).getGenome().getCities());
		CitiesPath child1 = new CitiesPath();
		CitiesPath child2 = new CitiesPath();

		for (int i = 0; i < breakpoint; i++) {
			child1.addCity(parent1Genome.getCities().get(i));
			child2.addCity(parent2Genome.getCities().get(i));
		}

		for (int i = breakpoint; i < genomeSize; i++) {
			if (!child1.getCities().contains(parent2Genome.getCities().get(i))) {
				child1.addCity(parent2Genome.getCities().get(i));
			}
			if (!child2.getCities().contains(parent1Genome.getCities().get(i))) {
				child2.addCity(parent1Genome.getCities().get(i));
			}
		}

		for (City city: CitiesManager.getInstance().getCities()) {
			if (!child1.getCities().contains(city))
				child1.addCity(city);
			if (!child2.getCities().contains(city))
				child2.addCity(city);
		}

		children.add(new Individual(child1));
		children.add(new Individual(child2));

		return children;
	}

	public Individual mutate(Individual individual) {
		Random random = new Random();
		float mutate = random.nextFloat();
		if (mutate < MUTATION_RATE) {
			CitiesPath genome = individual.getGenome();
			Collections.swap(genome.getCities(), random.nextInt(genomeSize-1)+1, random.nextInt(genomeSize-1)+1);
			return new Individual(genome);
		}
		return individual;
	}

	private List<Individual> selectBestIndividuals() {

		List<Individual> selected = new ArrayList<>();

		SelectionStrategy strategy = new RankSelection();
		//SelectionStrategy strategy = new RouletteWheelSelection();

		for (int i = 0; i < REPRODUCTION_SIZE; i++) {
			selected.add(strategy.select(population));
		}
		return selected;
	}


}


interface SelectionStrategy {
	public Individual select(Population population);
}

final class RouletteWheelSelection implements SelectionStrategy {

    @Override
    public Individual select(Population population) {
        double ball = Math.random();
        for (Individual individual : population.getIndividuals()) {
            double value = individual.getLength() / population.getTotalFitness();
            if (ball <= value) {
                return individual;
            }
            ball -= value;
        }
        throw new RuntimeException("Roulettewheel selection failed to select an individual");
    }
}

final class RankSelection implements SelectionStrategy {

	private List<Individual> previousSelected = new ArrayList<Individual>();

    @Override
    public Individual select(Population population) {
    	List<Individual> copyPopulation = new ArrayList<>(population.getIndividuals());
    	Individual selection = null;
    	do {
    		selection = Collections.min(copyPopulation);
    		copyPopulation.remove(selection);
    	} while(previousSelected.contains(selection));
    	previousSelected.add(selection);
    	return selection;
    }
}
