package brown.tac.adx.models;

import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.demand.AdNetworkDailyNotification;
import tau.tac.adx.report.demand.CampaignReport;
import brown.tac.adx.agents.CampaignData;

public interface ModelerAPI {
	public abstract double getCostForImpressions(String key, double impressions);  //key for this is tricky
	public abstract double getRevenueForEffectiveImpressions(int campaignID, double effectiveImpressions);
	public double getBidForImpressions(String key, double impressions);
	public AdxQuery[] getKeys();
	public Map<Integer, CampaignData> getCampaignMap();
	
	public void updateModeler(int day, AdxBidBundle bidBundle);
	public void updateModeler(int day, AdNetworkDailyNotification adNetDailyNotification);
	public void updateModeler(int day, CampaignReport campaignReport);
}
