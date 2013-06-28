package brown.tac.adx.optimizationalgs;

import java.util.LinkedList;

import brown.tac.adx.models.Model;

public abstract class OptimizationAlg {
	/*
	 * Takes in reference to model list so that algos can query the models
	 */
	protected LinkedList<Model> _modelList;
	
	/*
	 * Takes in optimizer ref to update its stored bid bundle
	 */
	protected Optimizer _optimizer;
	
	protected OptimizationAlg(LinkedList<Model> modelList, Optimizer optimizer){
		_modelList = modelList;
		_optimizer = optimizer;
	}
	abstract void makeDecision();
}
