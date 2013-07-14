package brown.tac.adx.agents;

import se.sics.tasim.props.SimulationStatus;
import se.sics.tasim.props.StartInfo;
import tau.tac.adx.props.PublisherCatalog;
//import tau.tac.adx.report.adn.AdNetworkReport;
import tau.tac.adx.report.demand.AdNetworkDailyNotification;
import tau.tac.adx.report.demand.CampaignOpportunityMessage;
import tau.tac.adx.report.demand.CampaignReport;
import tau.tac.adx.report.demand.InitialCampaignMessage;
//import tau.tac.adx.report.publisher.AdxPublisherReport;
import edu.umich.eecs.tac.props.BankStatus;

public class DailyInfo {
	
	private int _day;
	
	//only used day == 0
	StartInfo startInfo;
	PublisherCatalog publisherCatalog;
	InitialCampaignMessage initialCampaignInfo;
	
	//these are sent every day (even end days?)
	CampaignOpportunityMessage campaignOpportunityInfo;
	CampaignReport campaignReport;
	AdNetworkDailyNotification adNetDailyNotification;
	SimulationStatus simulationStatus;
	
	
	double qualityRating;
	
	BankStatus bankStatus;
	
	// Not used, as far as we know (Betsy)
	//AdxPublisherReport publisherReport;
	//AdNetworkReport adNetReport;
	
	public DailyInfo(int day) {
		_day = day;
	}
	
	public int getDay() {
		return _day;
	}
	
	public void setStartInfo(StartInfo startInfo){
		this.startInfo = startInfo;
	}
	
	public void setPublisherCatalog(PublisherCatalog publisherCatalog){
		this.publisherCatalog = publisherCatalog;
	}
	
	public void setInitialCampaignMessage(InitialCampaignMessage initialCampaignInfo){
		this.initialCampaignInfo = initialCampaignInfo;
	}

	public void setCampaignOpportunityMessage(CampaignOpportunityMessage campaignOpportunityInfo){
		this.campaignOpportunityInfo = campaignOpportunityInfo;
	}
	public void setCampaignReport(CampaignReport campaignReport){
		this.campaignReport = campaignReport;
	}
	public void setAdNetworkDailyNotification(AdNetworkDailyNotification adNetDailyNotification){
		this.adNetDailyNotification = adNetDailyNotification;
	}
	public void setSimulationStatus(SimulationStatus simulationStatus){
		this.simulationStatus = simulationStatus;
	}
	
	public void setQualityRating(double quality){
		this.qualityRating = quality;
	}
	
	public StartInfo getStartInfo(){
		if(_day==0){
			return startInfo;
		}else{
			return null;
		}
	}
	
	public PublisherCatalog getPublisherCatalog(){
		if(_day==0){
			return publisherCatalog;
		}else{
			return null;
		}
	}
	
	public InitialCampaignMessage getInitialCampaignMessage( ){
		if(_day==0){
			return initialCampaignInfo;
		}else{
			return null;
		}
	}

	public CampaignOpportunityMessage getCampaignOpportunityMessage(){
		return campaignOpportunityInfo;
	}
	public void getCampaignReport(CampaignReport campaignReport){
		this.campaignReport = campaignReport;
	}
	public AdNetworkDailyNotification getAdNetworkDailyNotification( ){
		return adNetDailyNotification;
	}
	public SimulationStatus getSimulationStatus(){
		return simulationStatus;
	}
	public double getQualityRating(){
		return this.qualityRating;
	}
}
