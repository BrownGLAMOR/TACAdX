package brown.tac.adx.optimization;

import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;

public class OptimizationMessenger {
	private AdxBidBundle _bidBundle;
	private double _ucsBid;
	private double _cmpBid;
	private AdxQuery[] _queries; //using Mariano's terminology -- equivalent to user-keys
	
	

	public OptimizationMessenger(){
		
	}
	
	public AdxQuery[] getQueries() {
		return _queries;
	}

	public void setQueries(AdxQuery[] queries) {
		_queries = queries;
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
