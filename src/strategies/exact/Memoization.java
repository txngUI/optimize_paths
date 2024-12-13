package strategies.exact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.City;

public class Memoization {
        
        private Map<City, Map<List<City>, Double>> memo;
        private int memoUsed;

        public Memoization() {
                super();
                memo = new HashMap<City, Map<List<City>, Double>>();
                memoUsed = 0;
        }

        public int getMemoUsed() {
                return memoUsed;
        }

        public void saveValueFor(City currentCity, List<City> cities2Visit, double minDistance) {
                if (memo.get(currentCity) == null) {
                        memo.put(currentCity, new HashMap<List<City>, Double>());

                }
                Map<List<City>, Double> memo4City = memo.get(currentCity);
                memo4City.put(cities2Visit, minDistance);

        }

        public Double getValueFor(City currentCity, List<City> cities2Visit) {
                if (cities2Visit == null || cities2Visit.isEmpty())
                        return null;
                if (memo.get(currentCity) == null)
                        return null;
                Map<List<City>, Double> memo4City = memo.get(currentCity);
                if (memo4City.get(cities2Visit) == null)
                        return null;
                return memo4City.get(cities2Visit);
        }
        

        public void incrementMemoUsed() {
                memoUsed++;
        }
        
        

}
















