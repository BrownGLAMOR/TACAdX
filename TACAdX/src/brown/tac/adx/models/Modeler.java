package brown.tac.adx.models;


import java.util.HashMap;
import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.AdNetworkKey;
import tau.tac.adx.report.adn.AdNetworkReport;
import tau.tac.adx.report.demand.AdNetworkDailyNotification;
import tau.tac.adx.report.demand.CampaignReport;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.models.revenue.RevenueModel;
//Many things to import for a DOM parser

/**
 * Class containing a list of models and updating them in order of dependencies
 */
public class Modeler implements ModelerAPI {
	
	/*
	 * List of models
	 */
//	LinkedList<Model> _modelList;
	
	CostModel _costModel;
	RevenueModel _revenueModel;
	
	public Modeler(Map<Integer, CampaignData> campaignMap) {
		_costModel = new CostModel();
		_revenueModel = new RevenueModel(campaignMap);
	}

	/* Modifies and returns the DailyPrediction object */
//	public DailyPrediction updateModels(DailyPrediction pred) {
//		for (Model model : _modelList) {
//			// This is meant to alter predictions with each iteration
//			// as the models get updated.
//			model.update(pred);
//		}
//		return pred;
//	}
//	public LinkedList<Model> getModelList(){
//		return _modelList;
//	}
	/*
	 * Parses the XML file filename and fills in the model list
	 */
	private void parseModels(String filename) {
		/*
//		 * ASSUMPTIONS: XML file is designed as follows: the tagged category is <model>. a model contains 
//		 * attributes of name (currently a string, but may be an enum), and parameters. <name> will be used 
//		 * to decide which model type to instantiate, and <parameter>s will be used in the instantiation
//		 */
//		//naive parser
//		try{
//			File fileToParse = new File(filename); 
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
//			Document document = dBuilder.parse(fileToParse); 
//			
//			NodeList elemList = document.getElementsByTagName("model");
//			for(int i=0; i<elemList.getLength(); i++){
//				Node currNode = elemList.item(i); 
//				if (currNode.getNodeType() == Node.ELEMENT_NODE){
//					Element element = (Element)currNode; 
//					if(element.getAttribute("name").equals("UserModel1")){	//will use enums later
//						//will be modified to efficiently call switch
//						brown.tac.adx.models.usermodels.UserModel1 um1 = new brown.tac.adx.models.usermodels.UserModel1(filename);
//						_modelList.add(um1); 
//				//etc with other explicit model names found in file
//						Model userModel = UserModel.build();
//					
//					}
//				}
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace(); 
//		}
		
	}

	@Override
	public double getCostForImpressions(AdxQuery key, double impressions) {
		return _costModel.getCostForImpressions(key, impressions);
	}
	
	@Override
	public double getRevenueForEffectiveImpressions(int campaignID,
			double effectiveImpressions) {
		return _revenueModel.getRevenueForEffectiveImpressions(effectiveImpressions, campaignID);
	}

	@Override
	public double getBidForImpressions(AdxQuery key, double impressions) {
		return _costModel.getBidForImpressions(key, impressions);
	}


	@Override
	public void updateModeler(int day, HashMap<AdNetworkKey, Double> bidBundle) {
		_costModel.updateModeler(day, bidBundle);
		
	}

	@Override
	public void updateModeler(int day, AdNetworkReport adNetReport) {
		_costModel.updateModeler(day, adNetReport);
		
	}
	
}
