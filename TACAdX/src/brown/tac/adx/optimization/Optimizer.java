package brown.tac.adx.optimization;

import java.util.LinkedList;
import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.models.ModelerAPI;
import brown.tac.adx.optimization.impressions.greedy.GreedyOptimizer;
import brown.tac.adx.optimization.impressions.greedy.ImpressionsOptimizer;

public class Optimizer {
	
	/*
	 * List of OptimizationAlgs
	 */
	LinkedList<OptimizationAlg> _optimizationAlgList;
	
	/*
	 * Bid Bundle for user impressions on day d+1
	 */
	AdxBidBundle _bidBundle;
	
	/*
	 * Modeler reference to query models from within OptAlgos
	 */
	ModelerAPI _modeler;
	
	ImpressionsOptimizer _impressionsOptimizer;
	
	
	public Optimizer(/*String filename,*/ ModelerAPI modeler, Map<Integer, CampaignData> campaignMap, AdxQuery[] keys) {
		_modeler = modeler;
		_impressionsOptimizer = new GreedyOptimizer(_modeler, campaignMap, keys);
//		_optimizationAlgList = new LinkedList<OptimizationAlg>();
//		_optimizationAlgList.add(new GreedyOptimizer(_modeler));
//		this.parseOptimizationAlgs(filename);
	}

	public void makeImpressionsDecision(){
		_impressionsOptimizer.makeDecision();
	}
	public AdxBidBundle getBidBundle(int day){
		return _impressionsOptimizer.getBidBundle(day);
	}
//	public void makeDecisions(DailyPrediction pred) {
//		for (OptimizationAlg alg : _optimizationAlgList) {
//			alg.makeDecision();
//		}
//	}
	
//	/*
//	 * Parses the XML file filename and fills in the optimizer list
//	 */
//	private void parseOptimizationAlgs(String filename) {
//		
//	}
}
