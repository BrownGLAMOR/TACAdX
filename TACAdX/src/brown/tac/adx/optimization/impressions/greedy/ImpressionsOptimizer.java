package brown.tac.adx.optimization.impressions.greedy;

import java.util.HashMap;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.report.adn.AdNetworkKey;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.models.ModelerAPI;
import brown.tac.adx.optimization.OptimizationAlg;


public abstract class ImpressionsOptimizer extends OptimizationAlg {

	protected ImpressionsOptimizer(ModelerAPI modeler) {
		super(modeler);
	}

	
	
	
	public abstract AdxBidBundle getBidBundle(int day);




	public abstract HashMap<AdNetworkKey, Double> getBidMapForCostModel(int day);
}
