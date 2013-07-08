package brown.tac.adx.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tau.tac.adx.ads.properties.AdType;
import tau.tac.adx.devices.Device;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.MarketSegment;
import brown.tac.adx.agents.CampaignData;

public class DummyModeler implements ModelerAPI {
	private AdxQuery[] _keys;
	private Map<Integer, CampaignData> _campaigns;
	public DummyModeler(){
		_keys = new AdxQuery[2];
		List<MarketSegment> markSegListOne = new ArrayList<MarketSegment>();
		markSegListOne.add(MarketSegment.FEMALE_YOUNG);
		_keys[0] = new AdxQuery("nyt", markSegListOne, Device.mobile, AdType.text);
	}

	@Override
	public double getCostForImpressions(String key, double impressions) {
		if (key==)
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
	
}
