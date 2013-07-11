package brown.tac.adx.models.costs;

public class SimpleLinearCostModel extends CostModelForKey {

	double _slope;
	double _yInt;
	public SimpleLinearCostModel(double slope, double yInt){
		_slope = slope;
		_yInt = yInt;
	}
	public double getCostForImpressions(double impressions) {
		return ((impressions*_slope)+_yInt);
	}

}


