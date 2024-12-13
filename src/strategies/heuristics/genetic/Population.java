package strategies.heuristics.genetic;

import java.util.ArrayList;
import java.util.List;


public class Population {

	private List<Individual> population;
	private double totalFitness;
	
	public Population() {
		population = new ArrayList<Individual>();
		totalFitness = 0;
	}

	public Individual getFirstIndividual() {
		return population.get(0);
	}

	public List<Individual> getIndividuals() {
		return population;
	}

	public void addIndividual(Individual individual) {
		population.add(individual);
		totalFitness += individual.getLength();
	}

	public void addSeveralIndividuals(List<Individual> individuals) {
		for (Individual individual: individuals) {
			addIndividual(individual);
		}	
	}

	public double getTotalFitness() {
		return totalFitness;
	}

	public int getSize() {
		return population.size();
	}
	
	
	
	
}
