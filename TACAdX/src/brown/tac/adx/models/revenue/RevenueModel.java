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
	public double getRevenueForEffectiveImpressions(double effectiveImpressions, int campaignId, int day) {
		CampaignData campaign = _campaignMap.get(campaignId);
		int daysRemaining = (int) (campaign.getEndDay()-day);
		return (1.0/daysRemaining)*getTrueDeltaRevenue(effectiveImpressions*daysRemaining, campaignId);
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
