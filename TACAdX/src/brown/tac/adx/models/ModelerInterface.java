package brown.tac.adx.models;

public interface ModelerInterface {
	public abstract double getCostForImpressions(String key, double impressions);
	public abstract double getRevenueForEffectiveImpressions(String campaignID, double effectiveImpressions);
	public double getBidForImpressions(String key, double impressions);
}
