package brown.tac.adx.optimization;

import tau.tac.adx.props.AdxBidBundle;

public class OptimizationMessenger {
	private AdxBidBundle _bidBundle;
	private double _ucsBid;
	private double _cmpBid;
	public OptimizationMessenger(){
		
	}
	
	public AdxBidBundle getBidBundle(){
		return _bidBundle;
	}
	public void setBidBundle(AdxBidBundle bidBundle){
		_bidBundle = bidBundle;
	}
	public double getUCS(){
		return _ucsBid;
	}
	public void setUCS(double ucsBid){
		_ucsBid = ucsBid;
	}
	public double getCampaignBid(){
		return _cmpBid;
	}
	public void setCampaignBid(double cmpBid){
		_cmpBid = cmpBid;
	}
}
