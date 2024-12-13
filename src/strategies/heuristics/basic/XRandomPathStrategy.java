package strategies.heuristics.basic;

import business.CitiesPath;
import strategies.PathStategy;
import strategies.Soluce;

public class XRandomPathStrategy extends PathStategy {

    private static final int NB_ITERATIONS = 1000;

    @Override
    protected CitiesPath runStrategy() {
        RandomPathStrategy randomPathStrategy = new RandomPathStrategy();
        Soluce currentBestSoluce = null;
        double currentMaxLength = Double.MAX_VALUE;

        for (int i = 0; i < NB_ITERATIONS; i++) {
            Soluce currentSoluce = randomPathStrategy.create();
            if (currentSoluce.getPath().getLength() < currentMaxLength) {
                currentBestSoluce = currentSoluce;
                currentMaxLength = currentSoluce.getPath().getLength();
            }
        }

        return currentBestSoluce.getPath();
    }
}
