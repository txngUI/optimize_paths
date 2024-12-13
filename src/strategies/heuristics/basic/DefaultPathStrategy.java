package strategies.heuristics.basic;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;

public class DefaultPathStrategy extends PathStategy {

	@Override
	protected CitiesPath runStrategy() {
		
		CitiesPath soluce = new CitiesPath();
		for (City city: CitiesManager.getInstance().getCities()) {
			soluce.addCity(city);
		}

		soluce.closeWithStartingCity();
		
		return soluce;
	}
}
