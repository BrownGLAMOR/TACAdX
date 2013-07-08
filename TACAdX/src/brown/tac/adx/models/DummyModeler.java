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

public class DummyModeler implements ModelerAPI {
	private AdxQuery[] _keys;
	private Map<Integer, CampaignData> _campaigns;
	public DummyModeler(){
		_keys = new AdxQuery[2];
		Set<MarketSegment> markSegListOne = new HashSet<MarketSegment>();
		markSegListOne.add(MarketSegment.FEMALE);
		markSegListOne.add(MarketSegment.YOUNG);
		_keys[0] = new AdxQuery("nyt", markSegListOne, Device.mobile, AdType.text);
		Set<MarketSegment> markSegListTwo = new HashSet<MarketSegment>();
		markSegListTwo.add(MarketSegment.MALE);
		markSegListTwo.add(MarketSegment.YOUNG);
		_keys[1] = new AdxQuery("espn", markSegListTwo, Device.pc, AdType.text);
		
		_campaigns = new HashMap<Integer, CampaignData>();
		Set<MarketSegment> cmpMarkSeg = new HashSet<MarketSegment>();
		cmpMarkSeg.add(MarketSegment.YOUNG);
		CampaignData cmp1 = new CampaignData(
				new InitialCampaignMessage(1, (long) 300, 0, 5, cmpMarkSeg, 1.2, 1.2));
		_campaigns.put(1, cmp1);
		
		CampaignData cmp2 = new CampaignData(
				new InitialCampaignMessage(2, (long) 400, 0, 5, cmpMarkSeg, 1.3,1.3));
		_campaigns.put(2,cmp2);

	}

	@Override
	public double getCostForImpressions(String key, double impressions) {
		if (key==_keys[0].toString()){
			continue;
		}
		else{
			
		}
	}

	@Override
	public double getRevenueForEffectiveImpressions(int campaignID,
			double effectiveImpressions) {
		return (5*effectiveImpressions)%100;
	}

	@Override
	public double getBidForImpressions(String key, double impressions) {
		// TODO Auto-generated method stub
		return 0;
	}

	public AdxQuery[] getKeys() {
		return _keys;
	}


	public Map<Integer, CampaignData> getCampaignMap() {
		return _campaigns;
	}
	
}
