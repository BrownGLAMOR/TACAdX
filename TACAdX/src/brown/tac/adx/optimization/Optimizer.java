package brown.tac.adx.optimization;

import java.util.LinkedList;

import tau.tac.adx.props.AdxBidBundle;
import brown.tac.adx.models.Modeler;
import brown.tac.adx.models.ModelerAPI;
import brown.tac.adx.optimization.impressions.greedy.GreedyOptimizer;
import brown.tac.adx.predictions.DailyPrediction;

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
	

	
	public Optimizer(String filename, ModelerAPI modeler) {
		_modeler = modeler;
		_optimizationAlgList = new LinkedList<OptimizationAlg>();
		_optimizationAlgList.add(new GreedyOptimizer(_modeler));
		this.parseOptimizationAlgs(filename);
	}

	public void makeDecisions(DailyPrediction pred) {
		for (OptimizationAlg alg : _optimizationAlgList) {
			alg.makeDecision();
		}
	}
	
	/*
	 * Parses the XML file filename and fills in the optimizer list
	 */
	private void parseOptimizationAlgs(String filename) {
		
	}
}
