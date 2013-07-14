package brown.tac.adx.models;

import brown.tac.adx.predictions.DailyPrediction;

public abstract class Model {
	
	public abstract void update(DailyPrediction prediction);
	
}
