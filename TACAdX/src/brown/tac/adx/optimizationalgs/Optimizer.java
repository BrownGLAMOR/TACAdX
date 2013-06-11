package brown.tac.adx.optimizationalgs;

import java.util.LinkedList;

import tau.tac.adx.props.AdxBidBundle;

import brown.tac.adx.models.Model;
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
	
	
	public Optimizer(String filename) {
		this.parseOptimizationAlgs(filename);
	}

	public void makeDecisions(DailyPrediction pred) {
		for (OptimizationAlg alg : _optimizationAlgList) {
			alg.makeDecision();
		}
	}
	
	public AdxBidBundle getBidBundle() {
		return _bidBundle;
	}
	
	/*
	 * Parses the XML file filename and fills in the optimizer list
	 */
	private void parseOptimizationAlgs(String filename) {
		
	}
}
