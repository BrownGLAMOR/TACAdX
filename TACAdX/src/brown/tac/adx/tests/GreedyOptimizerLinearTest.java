//package brown.tac.adx.tests;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import tau.tac.adx.props.AdxQuery;
//import brown.tac.adx.agents.CampaignData;
//import brown.tac.adx.models.DummyModeler;
//import brown.tac.adx.models.ModelerAPI;
//import brown.tac.adx.models.costs.CostModelForKey;
//import brown.tac.adx.models.costs.SimpleLinearCostModel;
//import brown.tac.adx.optimization.impressions.greedy.GreedyOptimizer;
//
//public class GreedyOptimizerLinearTest {
//
//	GreedyOptimizer _optimizer;
//	ModelerAPI _modeler;
//	CostModelForKey[] _costModels;
//	AdxQuery[] _keys;
//	Map<Integer, CampaignData> _campaignMap;
//	public GreedyOptimizerLinearTest() {
//	}
//
//
//	@Before
//	public void setUp() throws Exception {
//		
//	}
//	
//	@Test
//	public void linearTest1(){
//		_costModels = new CostModelForKey[2];
//		_costModels[0] = new SimpleLinearCostModel(4.0, 10.0);
//		_costModels[1] = new SimpleLinearCostModel(6.0, 0.0);
//		_modeler = new DummyModeler(_costModels);
//		_optimizer = new GreedyOptimizer(_modeler);
//		_keys = _modeler.getKeys();
//		_campaignMap = _modeler.getCampaignMap();
//		Object[] campaignListInterim = _campaignMap.keySet().toArray();
//		Integer[] campaignList = new Integer[_campaignMap.size()];
//		for (int i = 0; i<campaignList.length; i++){
//			campaignList[i] = (Integer) campaignListInterim[i];
//		}
//		double[][] allocation_kc = _optimizer.solve(_keys, _campaignMap, campaignList, 4, 2);
//		System.out.println("LO FINALE");
//		for (int c = 0; c<_campaignMap.size(); c++){
//			System.out.println(campaignList[c]);
//			for (int k = 0; k<_keys.length; k++){
//				System.out.println(allocation_kc[k][c]);
//			}
//		}
//	}
////	@Test
////	public void test() {
////		AdxQuery[] keys = _modeler.getKeys();
////		Map<Integer, CampaignData> campaignMap = _modeler.getCampaignMap();
////		Object[] campaignListInterm =   campaignMap.keySet().toArray();
////		Integer[] campaignList = new Integer[campaignMap.size()];
////		for (int i = 0; i< campaignList.length; i++){
////			campaignList[i] = (Integer) campaignListInterm[i];
////			System.out.println(campaignList[i]);
////		}
////		double[][] allocation_kc = _optimizer.solve(keys, campaignMap,campaignList, 10, 2);
////		for (int c = 0; c<campaignMap.size(); c++){
////			System.out.println(campaignList[c]);
////			for (int k = 0; k<keys.length; k++){
////				System.out.println(allocation_kc[k][c]);
////			}
////		}
////		System.out.println(allocation_kc);
////	}
//	
//	
//
//}
