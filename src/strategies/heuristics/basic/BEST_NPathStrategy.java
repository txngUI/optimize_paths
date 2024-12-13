package strategies.heuristics.basic;


import java.util.List;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import strategies.Soluce;

public class BEST_NPathStrategy extends PathStategy {

    @Override
    protected CitiesPath runStrategy() {
        CitiesPath currentBestPath = null;
        double currentBestPathLength = Double.MAX_VALUE;

        for(City city : CitiesManager.getInstance().getCities()) {
            CitiesPath currentPath = getNNSoluceStartingFrom(city);

            if(currentPath.getLength() < currentBestPathLength) {
                currentBestPath = currentPath;
                currentBestPathLength = currentPath.getLength();
            }
        }

        System.out.println(currentBestPath.getCityAt(0).getName());
        return currentBestPath;
    }
    private CitiesPath getNNSoluceStartingFrom(City startingCity) {
        CitiesPath path = new CitiesPath();
        List<City> remainingCities = CitiesManager.getInstance().getCities();

        City currentCity = startingCity;
        path.addCity(currentCity);

        while(!remainingCities.isEmpty()) {
            City nearestCity = currentCity.findClosestCityFrom(remainingCities);
            path.addCity(nearestCity);
            remainingCities.remove(nearestCity);
            currentCity = nearestCity;
        }

        path.closeWithStartingCity();
        return path;
    }
}
