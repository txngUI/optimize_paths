package strategies;

import strategies.exact.*;
import strategies.heuristics.ant.AntColonyStrategy;
import strategies.heuristics.basic.*;
import strategies.heuristics.genetic.GeneticStrategy;

public class StrategyManager {

    public static Soluce createSolucePath(SoluceType soluceType) {

        return switch (soluceType) {
            case DEFAULT -> new DefaultPathStrategy().create();
            case RANDOM -> new RandomPathStrategy().create();
            case XRANDOM -> new XRandomPathStrategy().create();
            case NN -> new NNNPathStrategy().create();
            case BestNN -> new BEST_NPathStrategy().create();
            case GENETIC -> new GeneticStrategy().create();
            case DFS -> new DfsStrategy().create();
            case DFS_PRECEDENCE -> new DfsPrecedencePathStrategy().create();
            case DFS_MAX -> new DfsMaxStrategy().create();
            case DFS_MINMAX -> new DfsMinMaxStrategy().create();
            case DP -> new DpStrategy().create();
            case ANT -> new AntColonyStrategy().create();
            default -> null;
        };
    }

}
