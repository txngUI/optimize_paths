package strategies.exact;

import java.util.ArrayList;
import java.util.List;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;

public class DpStrategy extends PathStategy {

    private City startingCity;
    private Memoization memo;

    @Override
    protected CitiesPath runStrategy() {
        startingCity = CitiesManager.getInstance().getCities().get(0);
        List<City> remainingCities = getRemainingCities();
        memo = new Memoization();
        double minDistance = searchMinDp(startingCity, remainingCities);

        System.out.println("Min distance: " + minDistance);

        return rebuildOptimalPathSoluce(minDistance);
    }

    private CitiesPath rebuildOptimalPathSoluce(double minDistance) {
        CitiesPath soluce = new CitiesPath();
        soluce.addCity(startingCity);
        List<City> remainingCities = getRemainingCities();

        while (remainingCities.isEmpty()) {
            City currentCity = soluce.getLastCity();
            double currentMin = Double.MAX_VALUE;
            City nextCity = null;
            for (City city : remainingCities) {
                double distance = CitiesManager.getInstance().distanceBetween(currentCity, city);
                double sum = memo.getValueFor(city, calculateRemainingCities(remainingCities, city)) + distance;
                if (sum < currentMin) {
                    currentMin = sum;
                    nextCity = city;
                }
            }
            soluce.addCity(nextCity);
            remainingCities = calculateRemainingCities(remainingCities, nextCity);
            
        }
        return soluce;
    }

    private List<City> getRemainingCities() {
        List<City> remainingCities = new ArrayList<>(CitiesManager.getInstance().getCities());
        remainingCities.remove(0);
        return remainingCities;
    }

    private double searchMinDp(City currentCity, List<City> remainingCities) {

        Double value = memo.getValueFor(startingCity, remainingCities);
        if (value != null) {
            return value;
        }
        double minDistance;
        if (remainingCities.isEmpty()) {
            minDistance = CitiesManager.getInstance().distanceBetween(currentCity, startingCity);
        } else {
            double currentMin = Double.MAX_VALUE;
            for (City NextCity : remainingCities) {
                double distance = CitiesManager.getInstance().distanceBetween(currentCity, NextCity);
                double sum = searchMinDp(NextCity, calculateRemainingCities(remainingCities, NextCity)) + distance;
                if (sum < currentMin) {
                    currentMin = sum;
                }
            }
            minDistance = currentMin;
        }

        memo.saveValueFor(currentCity, remainingCities, minDistance);

        return minDistance;

    }

    private List<City> calculateRemainingCities(List<City> remainingCities, City nextCity) {
        List<City> newRemainingCities = new ArrayList<>(remainingCities);
        newRemainingCities.remove(nextCity);
        return newRemainingCities;
    }
}
