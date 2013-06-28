package brown.tac.adx.optimization;

import java.util.LinkedList;

import tau.tac.adx.props.AdxBidBundle;
import brown.tac.adx.models.Model;
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
	LinkedList<Model> _modelList;
	
	public Optimizer(String filename, LinkedList<Model> modelList) {
		_modelList = modelList;
		_optimizationAlgList = new LinkedList<OptimizationAlg>();
		_optimizationAlgList.add(new GreedyOptimizer(_modelList, this));
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
	
	//Called by an opt algo.  It is centralized here so that 
	//the bid bundle generation is decoupled from the algo used
	public void generateBidBundleFromData(double[][] impAllocation){
		//TODO: Take in output of final opt algo, convert to usable bid bundle
	}
	
	/*
	 * Parses the XML file filename and fills in the optimizer list
	 */
	private void parseOptimizationAlgs(String filename) {
		
	}
}
