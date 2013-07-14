package brown.tac.adx.optimization;

import brown.tac.adx.models.ModelerAPI;

public abstract class OptimizationAlg {
	/*
	 * Takes in reference to modeler so that algos can query the models
	 */
	protected ModelerAPI _modeler;

	
	protected OptimizationAlg(ModelerAPI modeler){
		_modeler = modeler;
	}
	public abstract void makeDecision();
}
