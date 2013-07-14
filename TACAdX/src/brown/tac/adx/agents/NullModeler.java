package brown.tac.adx.agents;

import java.util.Map;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.demand.AdNetworkDailyNotification;
import tau.tac.adx.report.demand.CampaignReport;
import brown.tac.adx.models.ModelerAPI;

public class NullModeler implements ModelerAPI {
	
	public NullModeler(String filename) {
		
	}

	@Override
	public double getCostForImpressions(String key, double impressions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRevenueForEffectiveImpressions(int campaignID,
			double effectiveImpressions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getBidForImpressions(String key, double impressions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AdxQuery[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, CampaignData> getCampaignMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateModeler(int day, AdxBidBundle bidBundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateModeler(int day,
			AdNetworkDailyNotification adNetDailyNotification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateModeler(int day, CampaignReport campaignReport) {
		// TODO Auto-generated method stub
		
	}

}
