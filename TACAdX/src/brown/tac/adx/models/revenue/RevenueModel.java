package brown.tac.adx.models.revenue;

import java.util.Map;

import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.predictions.DailyPrediction;

public class RevenueModel extends RevenueModelForCampaign {
	
	private Map<Integer, CampaignData> _campaignMap;
	private double _a = 4.08577;
	private double _b = 3.08577;

	public RevenueModel(Map<Integer, CampaignData> campaignMap){
		_campaignMap = campaignMap;
	}
	
	//baselineImpressions is to allow for the functionality eric described in his proposal for accounting
	//for duration of campaigns.  this way, baseline can be an intermediate increment, not just
	//the amount of impressions the campaign started with that day
	public double getRevenueForEffectiveImpressions(double dayStartImps,double alreadyIncrementedImps, double newIncrementImps, int campaignId, int day) {
		CampaignData campaign = _campaignMap.get(campaignId);
		int daysRemaining = (int) (campaign.getEndDay()-day);
		return (1.0/daysRemaining)*(getTrueDeltaRevenue(dayStartImps+(newIncrementImps+alreadyIncrementedImps)*daysRemaining, campaignId)
				-getTrueDeltaRevenue(dayStartImps+(newIncrementImps*daysRemaining), campaignId));
	}
	
	public double getTrueDeltaRevenue(double effectiveImpressions, int campaignId){
		double reach = _campaignMap.get(campaignId).getReachImps();
		double err = (2/_a)*(Math.atan(_a*(effectiveImpressions/reach)-_b)-Math.atan(-1*_b));
		return err*_campaignMap.get(campaignId).getBudget();
	}
	

	@Override
	public void update(DailyPrediction prediction) {
		// TODO Auto-generated method stub
		
	}

}
