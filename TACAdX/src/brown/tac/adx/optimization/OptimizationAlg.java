package brown.tac.adx.optimization;

import brown.tac.adx.models.ModelerAPI;

public abstract class OptimizationAlg {
	/*
	 * Takes in reference to modeler so that algos can query the models
	 */
	protected ModelerAPI _modeler;
	/*
	 * Takes in optimization messenger to liase between opt algos and agent
	 */
	protected OptimizationMessenger _optMessenger;
	
	protected OptimizationAlg(ModelerAPI modeler, OptimizationMessenger optMessenger){
		_modeler = modeler;
		_optMessenger = optMessenger;
	}
	public abstract void makeDecision();
}
