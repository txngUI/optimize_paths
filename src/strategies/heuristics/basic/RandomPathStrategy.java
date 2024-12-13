package strategies.heuristics.basic;

import java.util.List;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;

public class RandomPathStrategy extends PathStategy {

    @Override
    protected CitiesPath runStrategy() {

        CitiesPath soluce = new CitiesPath();

        List<City> remainingCities = CitiesManager.getInstance().getCities();
        while (!remainingCities.isEmpty()) {
            int index = (int) (Math.random() * remainingCities.size());
            City city = remainingCities.remove(index);
            soluce.addCity(city);
        }
        soluce.closeWithStartingCity();

        return soluce;
    }
}
