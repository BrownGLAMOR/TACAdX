package brown.tac.adx.agents;

import tau.tac.adx.props.AdxBidBundle;
import brown.tac.adx.models.ModelerAPI;
import brown.tac.adx.optimization.Optimizer;

public class NullOptimizer extends Optimizer {

	public NullOptimizer(String filename, ModelerAPI modeler) {
		super(filename, modeler);
	}
	
	@Override
	public AdxBidBundle getBidBundle(int day) {
		return null;
	}

}
