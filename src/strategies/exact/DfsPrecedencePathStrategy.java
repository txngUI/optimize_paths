package strategies.exact;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import ui.DisplayPanel;

public class DfsPrecedencePathStrategy extends PathStategy {

    private PartialPath bestPath;
    private City firstCity;
    private City secondCity;
    private int cptCompletePath;

    @Override
    protected CitiesPath runStrategy() {

        City startingCity = CitiesManager.getInstance().getCity(0);
        PartialPath initialPath = new PartialPath(startingCity);
        bestPath = null;
        cptCompletePath = 0;
        firstCity = CitiesManager.getInstance().getCity(1);
        secondCity = CitiesManager.getInstance().getCity(2);

        dfs(initialPath);

        System.out.println("Nb complete paths: " + cptCompletePath);

        return bestPath.getVisitedCities();
    }

    private void dfs(PartialPath path) {
        if (path.isComplete()) {
            cptCompletePath++;
            path.goBackStartingCity();
            if (bestPath == null || path.getTotalDistance() < bestPath.getTotalDistance()) {
                bestPath = path;
                DisplayPanel.instance.setPath2display(bestPath.getVisitedCities());
            }
        } else {
            for (City remainingCity : path.getUnvisitedCities()) {
                if (!remainingCity.equals(firstCity) || path.alreadyVisited(secondCity)) {
                    PartialPath newPartialPath = new PartialPath(path, remainingCity);
                    dfs(newPartialPath);
                }
            }
        }
    }
}
