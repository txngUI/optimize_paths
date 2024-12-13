package strategies.exact;

import business.CitiesPath;
import business.City;
import input.CitiesManager;
import strategies.PathStategy;
import ui.DisplayPanel;

public class DfsStrategy extends PathStategy {

    private PartialPath bestPath;
    private int cptCitiesPath = 0;


    @Override
    protected CitiesPath runStrategy() {

        City startCity = CitiesManager.getInstance().getCities().get(0);
        PartialPath initialPath = new PartialPath(startCity);
        bestPath = null;
        cptCitiesPath = 0;
        dfs(initialPath);

        System.out.println("Nombre de chemins parcourus : " + cptCitiesPath);
        return bestPath.getVisitedCities();
    }

    public void dfs(PartialPath path) {
        if (path.isComplete()) {
            cptCitiesPath++;
            path.goBackStartingCity();
            if (bestPath == null || path.getTotalDistance() < bestPath.getTotalDistance()) {
                bestPath = path;
                DisplayPanel.instance.setPath2display(bestPath.getVisitedCities());
            }
        } else {
            for (City remainingCity : path.getUnvisitedCities()) {
                PartialPath newPath = new PartialPath(path, remainingCity);
                dfs(newPath);
            }
        }
    }
}
