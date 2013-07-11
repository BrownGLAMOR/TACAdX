package brown.tac.adx.models.costs;

import java.util.HashMap;
import java.util.Map;


public class DiscreteCostModelForKeyOne extends CostModelForKey {

	public DiscreteCostModelForKeyOne(){
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
		costMap.put(0.0, 0.0);
		costMap.put(5.0, 6.0);
		costMap.put(10.0, 12.0);
		costMap.put(15.0, 18.0);
		costMap.put(20.0, 24.0);
		costMap.put(25.0, 30.0);
		costMap.put(30.0, 36.0);
	}

}

