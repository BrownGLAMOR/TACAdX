package brown.tac.adx.models.costs;

import java.util.HashMap;
import java.util.Map;

/*
 * Generates and stores random numbers for slope and y-intercept.
 * Uses these as linear model variables to compute cost for impressions.
 */
public class DiscreteCostModelForKeyOne extends CostModelForKey {

	public DiscreteCostModelForKeyOne(){
	}
	public double getCostForImpressions(double impressions) {
		return costMap.get(impressions);
	}
	
	private static final Map<Double,Double> costMap;
	static{
		costMap = new HashMap<Double, Double>();
		costMap.put(0.0, 0.0);
		costMap.put(5.0, 6.0);
		costMap.put(10.0, 12.0);
		costMap.put(15.0, 18.0);
		costMap.put(20.0, 24.0);
	}

}
