package brown.tac.adx.optimization;

import brown.tac.adx.models.Modeler;

public abstract class OptimizationAlg {
	/*
	 * Takes in reference to modeler so that algos can query the models
	 */
	protected Modeler _modeler;
	/*
	 * Takes in optimizer ref to update its stored bid bundle
	 */
	protected Optimizer _optimizer;
	
	protected OptimizationAlg(Modeler modeler, Optimizer optimizer){
		_modeler = modeler;
		_optimizer = optimizer;
	}
	abstract void makeDecision();
}
