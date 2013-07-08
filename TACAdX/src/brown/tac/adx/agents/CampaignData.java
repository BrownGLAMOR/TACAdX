package brown.tac.adx.agents;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import tau.tac.adx.ads.properties.AdType;
import tau.tac.adx.demand.CampaignStats;
import tau.tac.adx.devices.Device;
import tau.tac.adx.report.adn.MarketSegment;
import tau.tac.adx.report.demand.CampaignOpportunityMessage;
import tau.tac.adx.report.demand.InitialCampaignMessage;

public class CampaignData {
	/* campaign attributes as set by server */
	Long reachImps;
	long dayStart;
	long dayEnd;
	Set<MarketSegment> targetSegments;
	double videoCoef;
	double mobileCoef;
	int id;

	/* campaign info as reported */
	CampaignStats stats;
	double budget;

	public CampaignData(InitialCampaignMessage icm) {
		reachImps = icm.getReachImps();
		dayStart = icm.getDayStart();
		dayEnd = icm.getDayEnd();
		targetSegments = icm.getTargetSegment();
		videoCoef = icm.getVideoCoef();
		mobileCoef = icm.getMobileCoef();
		id = icm.getId();

		stats = new CampaignStats(0, 0, 0);
		budget = 0.0;
	}

	public void setBudget(double d) {
		budget = d;
	}
	
	public int getId(){
		return id;
	}
	
	public double effectiveImps(){
		return stats.getTargetedImps();
	}

	public CampaignData(CampaignOpportunityMessage com) {
		dayStart = com.getDayStart();
		dayEnd = com.getDayEnd();
		id = com.getId();
		reachImps = com.getReachImps();
		targetSegments = com.getTargetSegment();
		mobileCoef = com.getMobileCoef();
		videoCoef = com.getVideoCoef();
		stats = new CampaignStats(0, 0, 0);
		budget = 0.0;
	}
	
	public boolean isFeasibleToAllocate(Set<MarketSegment> segmentsList){
		Set<MarketSegment> intersection = new HashSet<MarketSegment>(segmentsList);
		intersection.retainAll(targetSegments);   //nondestructively calculating intersection
		return intersection.size()>0;
	}
	
	public double effectiveImpressionsMultiplier(AdType adType, Device device){
		double multiplier = 1;
		if (adType==AdType.video){
			multiplier*=videoCoef;
		}
		if (device==Device.mobile){
			multiplier*=mobileCoef;
		}
		return multiplier;
		
	}

	@Override
	public String toString() {
		return "Campaign ID " + id + ": " + "day " + dayStart + " to "
				+ dayEnd + " " + Arrays.toString(targetSegments.toArray()) + ", reach: "
				+ reachImps + " coefs: (v=" + videoCoef + ", m="
				+ mobileCoef + ")";
	}

	int impsTogo() {
		return (int) Math.max(0, reachImps - stats.getTargetedImps());
	}

	void setStats(CampaignStats s) {
		stats.setValues(s);
	}

}