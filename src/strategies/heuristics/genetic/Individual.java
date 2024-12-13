package strategies.heuristics.genetic;

import java.util.Collections;
import java.util.List;

import business.CitiesPath;
import business.City;
import input.CitiesManager;

public class Individual implements Comparable<Individual> {

	private final CitiesPath genome;

	public Individual() {
		super();
		genome = randomPath();
	}

	public Individual(CitiesPath parentGenome) {
		genome = new CitiesPath(parentGenome.getCities());
	}

	private CitiesPath randomPath() {
		List<City> orderedThenShuffled = CitiesManager.getInstance().getCities();
	    Collections.shuffle(orderedThenShuffled);

	    return new CitiesPath(orderedThenShuffled);
	}

	public CitiesPath getGenome() {
		return genome;
	}

	public double getLength() {
		return genome.getLength();
	}

	public int compareTo(Individual otherGenome) {
		if(getLength() > otherGenome.getLength())
            return 1;
        else if(getLength() < otherGenome.getLength())
            return -1;
        else
            return 0;
    }
}
