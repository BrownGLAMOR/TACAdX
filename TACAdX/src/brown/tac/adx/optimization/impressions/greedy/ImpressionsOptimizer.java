package brown.tac.adx.optimization.impressions.greedy;

import tau.tac.adx.props.AdxBidBundle;
import brown.tac.adx.models.Modeler;
import brown.tac.adx.optimization.OptimizationAlg;
import brown.tac.adx.optimization.OptimizationMessenger;


public abstract class ImpressionsOptimizer extends OptimizationAlg {

	protected ImpressionsOptimizer(Modeler modeler, OptimizationMessenger optMessenger) {
		super(modeler, optMessenger);
	}

	
	
	
	public AdxBidBundle generateBidBundle(double[][] imps){
		//TODO: generate a bid bundle
		return null;
		
	}
}
