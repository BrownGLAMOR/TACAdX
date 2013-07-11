package brown.tac.adx.models;

import tau.tac.adx.props.AdxQuery;

public interface ModelerInterface {
	public abstract double getCostForImpressions(AdxQuery key, double impressions);
	public abstract double getRevenueForEffectiveImpressions(String campaignID, double effectiveImpressions);
	public double getBidForImpressions(AdxQuery key, double impressions);
}
