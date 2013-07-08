package brown.tac.adx.optimization.impressions.greedy;

import tau.tac.adx.props.AdxBidBundle;
import brown.tac.adx.models.Modeler;
import brown.tac.adx.models.ModelerAPI;
import brown.tac.adx.optimization.OptimizationAlg;


public abstract class ImpressionsOptimizer extends OptimizationAlg {

	protected ImpressionsOptimizer(ModelerAPI modeler) {
		super(modeler);
	}

	
	
	
	public AdxBidBundle generateBidBundle(double[][] imps){
		
		return null;
		
	}
}
