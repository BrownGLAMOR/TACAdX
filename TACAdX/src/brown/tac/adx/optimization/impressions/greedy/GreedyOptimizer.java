package brown.tac.adx.optimization.impressions.greedy;

import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.models.ModelerAPI;
import edu.umich.eecs.tac.props.Ad;

/**
 * A simple greedy algorithm to the AdX optimization problem. For a given problem instance,
 * the algorithm greedily adds incremental effective impressions to the most profitable campaign,
 * until no more incremental changes are profitable. There are two greedy choices being made:
 * (1) The highest profit increment is greedily chosen at each step.
 * (2) To compute the costs associated with a given increment, the increment is split up into
 *     sub-increments, and the lowest cost key for each sub-increment is greedily chosen.
 * @author sodomka
 */
public class GreedyOptimizer extends ImpressionsOptimizer{
	
	public GreedyOptimizer(ModelerAPI modeler) {
		super(modeler);
	}

	public void makeDecision(){
		AdxQuery[] keys = _modeler.getKeys();
		Map<Integer, CampaignData> campaignMap = _modeler.getCampaignMap();
		Integer[] campaignList = (Integer[]) campaignMap.keySet().toArray();
		double[][] allocation_kc = this.solve(keys, campaignMap,campaignList, 5, 2);
		AdxBidBundle bidBundle = new AdxBidBundle();
		for (int k = 0; k<keys.length;k++){
			for (int c = 0; c<campaignList.length; c++){
				bidBundle.addQuery(keys[k], _modeler.getBidForImpressions(keys[k].toString(), allocation_kc[k][c]),
					new Ad(null), campaignList[c], (int)Math.round(allocation_kc[k][c])); //weight is equivalent to normalized value of bid
			}
		}
		
	}
	
	/**  
	 * Greedily solves a given problem instance.
	 * @param revenueModelForCampaign_c  An array of revenue models, one per campaign. 
	 * @param costModelForKey_k  An array of cost models, one per key.
	 * @param isFeasibleToAllocate_kc  A matrix of booleans, where isFeasibleToAllocate_kc[k][c] specifies
	 *  whether or not it is feasible to allocate impressions from key k to campaign c.
	 * @param effectiveImpressionsMultiplier_kc  A matrix of multipliers, where 
	 *  effectiveImpressionsMultiplier_kc[k][c] specifies the amount to multiply impressions by to get 
	 *  effective impressions from key k to campaign c. 
	 * @param effectiveImpressionIncrement  The number of effective impressions the greedy algorithm
	 *  considers incrementing each campaign by at each step.  
	 * @param numSubIncrements  The number of increments that effectiveImpressionIncrement is broken
	 *  into in order to compute the lowest costs associated with making some increment. 
	 * @return A matrix of doubles, where x_kc[k][c] specifies the number of impressions to allocate
	 *  from key k to campaign c.
	 */
	public double[][] solve(
			AdxQuery[] keys,
			Map<Integer, CampaignData> campaignMap,
			Integer[] campaignList, //That this is the same for caller and callee eases construction of bid bundles
			double effectiveImpressionIncrement,
			int numSubIncrements
			) {
		int numKeys = keys.length;
		int numCampaigns = campaignMap.size();
		double effectiveImpressionSubIncrement = effectiveImpressionIncrement / numSubIncrements;
		
		double[][] x_kc = new double[numKeys][numCampaigns]; // number of impressions to allocate from key k to campaign c.
		double[] y_k = new double[numKeys]; // sum of impressions from key k.
		double[] z_c = new double[numCampaigns]; // sum of effective impressions for campaign c.

		boolean incrementsProfitable = true;
		while (incrementsProfitable) {
			double profitForBestIncrement = Double.NEGATIVE_INFINITY;
			double[][] bestIncrement_kc = null;

			// Find the next campaign to increment.
			for (int c=0; c<numCampaigns; c++) {
				// Get revenue for incrementing that campaign
				double revenueForIncrement = _modeler.getRevenueForEffectiveImpressions(campaignList[c], z_c[c]+effectiveImpressionIncrement)-
						_modeler.getRevenueForEffectiveImpressions(campaignList[c], z_c[c]);

				// Get costs associated with incrementing that campaign.
				double costForIncrement = 0;
				double[][] potentialIncrement_kc = new double[numKeys][numCampaigns];
				CampaignData currCampaign = campaignMap.get(campaignList[c]);
				for (int i=0; i<numSubIncrements; i++) {
					// Choose the sub-increment with the lowest marginal cost
					double cheapestSubIncrementCost = Double.POSITIVE_INFINITY;
					int cheapestSubIncrementKey = -1;
					for (int k=0; k<numKeys; k++) {
						AdxQuery currKey = keys[i];
						if (currCampaign.isFeasibleToAllocate(keys[k].getMarketSegments())) {
							double impressionSubIncrement = effectiveImpressionSubIncrement /
									currCampaign.effectiveImpressionsMultiplier(currKey.getAdType(), currKey.getDevice());
							double subIncrementCost = 
									_modeler.getCostForImpressions(currKey.toString(),y_k[k]+impressionSubIncrement+potentialIncrement_kc[k][c])-
									_modeler.getCostForImpressions(currKey.toString(), y_k[k] + potentialIncrement_kc[k][c]);
							if (subIncrementCost < cheapestSubIncrementCost) {
								cheapestSubIncrementKey = k;
								cheapestSubIncrementCost = subIncrementCost;
							}
						}
					}

					double impressionSubIncrement = effectiveImpressionSubIncrement / 
							currCampaign.effectiveImpressionsMultiplier(keys[cheapestSubIncrementKey].getAdType(), keys[cheapestSubIncrementKey].getDevice());
					potentialIncrement_kc[cheapestSubIncrementKey][c] += impressionSubIncrement;
					costForIncrement += cheapestSubIncrementCost;
				}

				double profitForPotentialIncrement = revenueForIncrement - costForIncrement;
				if (profitForPotentialIncrement > profitForBestIncrement) {
					profitForBestIncrement = profitForPotentialIncrement;
					bestIncrement_kc = potentialIncrement_kc;
				}
			}

			if (profitForBestIncrement > 0) {
				// Make the increment
				for (int k=0; k<numKeys; k++) {
					for (int c=0; c<numCampaigns; c++) {
						x_kc[k][c] += bestIncrement_kc[k][c];
						y_k[k] += bestIncrement_kc[k][c];
						z_c[c] += bestIncrement_kc[k][c];
					}
				}
			} else {
				incrementsProfitable = false;
			}
		}

		return x_kc;
	}
}
