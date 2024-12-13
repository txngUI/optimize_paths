package strategies.exact;

import business.CitiesPath;
import business.City;
import input.CitiesManager;

import java.util.*;

public class PartialPath {

    private CitiesPath visitedCities;
    private Set<City> unvisitedCities;

    public PartialPath() {
        super();
        visitedCities = new CitiesPath();
        unvisitedCities = new HashSet<>(CitiesManager.getInstance().getCities());
    }

    public PartialPath(City startCity) {
        this();
        addCity(startCity);
    }

    private void addCity(City startCity) {
        visitedCities.addCity(startCity);
        unvisitedCities.remove(startCity);
    }

    public PartialPath(PartialPath path, City city) {
        visitedCities = new CitiesPath(path.getVisitedCities());
        unvisitedCities = new HashSet<>(path.getUnvisitedCities());
        addCity(city);
    }

    public Set<City> getUnvisitedCities() {
        return unvisitedCities;
    }


    public CitiesPath getVisitedCities() {
        return visitedCities;
    }

    public boolean isComplete() {
        return unvisitedCities.isEmpty();
    }

    public double getTotalDistance() {
        return visitedCities.getLength();
    }

    public void goBackStartingCity() {
        visitedCities.closeWithStartingCity();
    }

    public boolean alreadyVisited(City city) {
        return visitedCities.getCities().contains(city);
    }

    public boolean respectMaxBound(PartialPath bestPath) {
        if (bestPath == null) {
            return true;
        }
        return getTotalDistance() < bestPath.getTotalDistance();
    }

    public boolean respectMinBound(PartialPath bestPath) {
        if (bestPath == null) {
            return true;
        }
        List<Double> allEdges = new ArrayList<>(CitiesManager.getInstance().getAllEdges());

        List<Double> allRemainEdges = removeUsedEdges(allEdges);
        Collections.sort(allRemainEdges);

        double bestFuture = calculateBestFurure(allRemainEdges);
        double hypotheticBestPath = getTotalDistance() + bestFuture;
        return hypotheticBestPath < bestPath.getTotalDistance();
    }

    private double calculateBestFurure(List<Double> allRemainEdges) {
        int nbRemainingCities = unvisitedCities.size() + 1;
        double sum = 0;
        for (int i = 0; i < nbRemainingCities; i++) {
            sum += allRemainEdges.get(i);
        }

        return sum;
    }

    private List<Double> removeUsedEdges(List<Double> allEdges) {

        City lastCity = null;

        for (City currentCity : visitedCities.getCities()) {
            if (lastCity != null) {
                allEdges.remove(CitiesManager.getInstance().distanceBetween(lastCity, currentCity));
            }
            lastCity = currentCity;
        }

        return allEdges;
    }
}