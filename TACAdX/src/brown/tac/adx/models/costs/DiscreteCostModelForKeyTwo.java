package brown.tac.adx.models.costs;

import java.util.HashMap;
import java.util.Map;

/*
 * Generates and stores random numbers for slope and y-intercept.
 * Uses these as linear model variables to compute cost for impressions.
 */
public class DiscreteCostModelForKeyTwo extends CostModelForKey {

	public DiscreteCostModelForKeyTwo(){
	}
	public double getCostForImpressions(double impressions) {
		if (costMap.containsKey(impressions)){
			return costMap.get(impressions);
		}
		else{
			return Double.MAX_VALUE;
		}
	}
	
	private static final Map<Double,Double> costMap;
	static{
		costMap = new HashMap<Double, Double>();
		costMap.put(0.0, 5.0);
		costMap.put(5.0, 10.0);
		costMap.put(10.0, 15.0);
		costMap.put(15.0, 20.0);
		costMap.put(20.0, 25.0);
		costMap.put(25.0, 30.0);
		costMap.put(30.0, 35.0);
	}

}
