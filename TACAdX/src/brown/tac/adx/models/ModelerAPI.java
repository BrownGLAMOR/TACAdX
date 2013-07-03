package brown.tac.adx.models;

import java.util.List;
import java.util.Map;

import brown.tac.adx.agents.CampaignData;

import tau.tac.adx.props.AdxQuery;

public interface ModelerAPI {
	public abstract double getCostForImpressions(String key, double impressions);
	public abstract double getRevenueForEffectiveImpressions(String campaignID, double effectiveImpressions);
	public double getBidForImpressions(String key, double impressions);
	public List<AdxQuery> getKeys();
	public Map<Integer, CampaignData> getCampaignMap();
}
