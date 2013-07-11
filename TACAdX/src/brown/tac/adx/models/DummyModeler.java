package brown.tac.adx.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tau.tac.adx.ads.properties.AdType;
import tau.tac.adx.devices.Device;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.MarketSegment;
import tau.tac.adx.report.demand.InitialCampaignMessage;
import brown.tac.adx.agents.CampaignData;
import brown.tac.adx.models.costs.CostModelForKey;
import brown.tac.adx.models.costs.DiscreteCostModelForKeyOne;
import brown.tac.adx.models.costs.DiscreteCostModelForKeyTwo;

public class DummyModeler implements ModelerAPI {
	private AdxQuery[] _keys;
	private Map<Integer, CampaignData> _campaigns;
	private CostModelForKey[] _costModels;
	private long _maxCampaignImpressions = 12;
	public DummyModeler(CostModelForKey[] costModels){
		_costModels = costModels;
		_keys = new AdxQuery[2];
		Set<MarketSegment> markSegListOne = new HashSet<MarketSegment>();
		markSegListOne.add(MarketSegment.YOUNG);
		_keys[0] = new AdxQuery("nyt", markSegListOne, Device.mobile, AdType.text);
		Set<MarketSegment> markSegListTwo = new HashSet<MarketSegment>();
		markSegListTwo.add(MarketSegment.YOUNG);
		_keys[1] = new AdxQuery("espn", markSegListTwo, Device.pc, AdType.text);
		
		_campaigns = new HashMap<Integer, CampaignData>();
		Set<MarketSegment> cmpMarkSeg = new HashSet<MarketSegment>();
		cmpMarkSeg.add(MarketSegment.YOUNG);
		CampaignData cmp1 = new CampaignData(
				new InitialCampaignMessage(1, _maxCampaignImpressions, 0, 5, cmpMarkSeg, 1, 1));
		_campaigns.put(1, cmp1);
		
		CampaignData cmp2 = new CampaignData(
				new InitialCampaignMessage(2, _maxCampaignImpressions, 0, 5, cmpMarkSeg, 1,1));
		_campaigns.put(2,cmp2);

	}

	@Override
	public double getCostForImpressions(String key, double impressions) {
		if (key.equals(_keys[1].toString())){
			return _costModels[1].getCostForImpressions(impressions);
		}
		else{
			return _costModels[0].getCostForImpressions(impressions);
		}
	}

	@Override
	public double getRevenueForEffectiveImpressions(int campaignID,
			double effectiveImpressions) {
		if (effectiveImpressions<=_maxCampaignImpressions){
			return 6*effectiveImpressions;
		}
		else{
			return 6*_maxCampaignImpressions;
		}
	}

	@Override
	public double getBidForImpressions(String key, double impressions) {
		return this.getCostForImpressions(key, impressions) + 5;
	}

	public AdxQuery[] getKeys() {
		return _keys;
	}


	public Map<Integer, CampaignData> getCampaignMap() {
		return _campaigns;
	}
	
}
