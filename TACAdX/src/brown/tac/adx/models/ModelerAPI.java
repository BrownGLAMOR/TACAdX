package brown.tac.adx.models;

import java.util.HashMap;
import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.AdNetworkKey;
import tau.tac.adx.report.adn.AdNetworkReport;
import tau.tac.adx.report.demand.CampaignReport;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.optimization.Optimizer;

public interface ModelerAPI {
	public abstract double getCostForImpressions(AdxQuery key, double impressions);  //key for this is tricky
	public abstract double getRevenueForEffectiveImpressions(double dayStartImps,double alreadyIncrementedImps, double newIncrementEffectiveImps, int campaignId, int day);
	public double getBidForImpressions(AdxQuery key, double impressions);
	
	public AdNetworkKey queryToKey(AdxQuery adxQuery, int campaignID);

	
	public void updateModeler(int day, HashMap<AdNetworkKey, Double> bidBundle);
	public void updateModeler(int day, AdNetworkReport adNetworkReport);
	public abstract void setOptimizer(Optimizer optimizer);
}
