package strategies.exact;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import ui.DisplayPanel;

public class DfsMaxStrategy extends PathStategy {
    private PartialPath bestPath;
    private City firstCity;
    private City secondCity;
    private int cptCompletePath;
    private int cptMaxCuts;

    @Override
    protected CitiesPath runStrategy() {
        City startCity = CitiesManager.getInstance().getCity(0);
        PartialPath parcoursInitial = new PartialPath(startCity);
        bestPath = null;

        cptCompletePath = 0;
        cptMaxCuts = 0;

        firstCity = CitiesManager.getInstance().getCity(1);
        secondCity = CitiesManager.getInstance().getCity(2);
        dfs(parcoursInitial);

        System.out.println("Nb complete path: " + cptCompletePath);
        System.out.println("Nb complete path (max): " + cptMaxCuts);

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
            if(!path.respectMaxBound(bestPath)){
                cptMaxCuts++;
                return;
            }
            for (City remainingCity : path.getUnvisitedCities()) {
                if (!remainingCity.equals(firstCity) || !path.alreadyVisited(secondCity)) {
                    PartialPath newPath = new PartialPath(path, remainingCity);
                    dfs(newPath);
                }

            }
        }
    }
}