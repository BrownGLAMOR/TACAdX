package brown.tac.adx.tests;



import brown.tac.adx.models.DummyModeler;
import brown.tac.adx.optimization.impressions.greedy.GreedyOptimizer;
import junit.framework.*;


public class GreedyOptimizerToyTest extends TestCase{
	public  GreedyOptimizerToyTest(){
		DummyModeler toyModel = new DummyModeler();
		GreedyOptimizer optAlg = new GreedyOptimizer(toyModel);
		
	}
	
	
}
