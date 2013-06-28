package brown.tac.adx.models.costs;

public class LinearCostModelForKey extends CostModelForKey {

	private double _yIntercept;
	private double _slope;
	public LinearCostModelForKey(){
		_slope = Math.random()*0.2; //TODO change 0.2, it is arbitrary
		_yIntercept = Math.random()*20+5; //TODO change 20, it is arbitrary
	}
	public double getCostForImpressions(double impressions) {
		return impressions*_slope+_yIntercept;
	}

}
