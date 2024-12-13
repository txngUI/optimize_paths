package strategies.heuristics.basic;


import java.util.List;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;

public class NNNPathStrategy extends PathStategy {

    @Override
    protected CitiesPath runStrategy() {

        CitiesPath soluce = new CitiesPath();
        List<City> remaingCities = CitiesManager.getInstance().getCities();

        City currentCity = remaingCities.remove(0);
        soluce.addCity(currentCity);

        while(!remaingCities.isEmpty()) {
            City nextCity = currentCity.findClosestCityFrom(remaingCities);
            soluce.addCity(nextCity);
            remaingCities.remove(nextCity);
            currentCity = nextCity;
        }

        soluce.closeWithStartingCity();
        return soluce;
    }
}
