package strategies;

import business.CitiesPath;

public abstract class PathStategy {

	public final Soluce create() {
		long startTime = System.currentTimeMillis();

		CitiesPath soluce = runStrategy();

		long endTime = System.currentTimeMillis();

		long duration = endTime - startTime;

		return new Soluce(soluce, duration);
	}

	protected abstract CitiesPath runStrategy();

}
