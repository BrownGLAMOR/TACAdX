package brown.tac.adx.models;

import tau.tac.adx.report.adn.AdNetworkKey;

/**
 * Generic model, with TBD regression function - pulls campaign/key data and produces a scatterplot
 * that can be used to predict different values of x/y.
 * 
 * Update: This class should offer both regression models, as it has all the data anyways!
 */
public class CostModelEntry{
	private double confidence;
	private RegressionInfo linReg;
	private final AdNetworkKey key;
	
	public CostModelEntry(){
		this.key = new AdNetworkKey();
	}
	public CostModelEntry(AdNetworkKey key){
		this.key = key;
	}
	
	public double getConfidence(){
		return confidence;
	}
	public AdNetworkKey getKey(){
		return key;
	}
	public void setLinearRegression(RegressionInfo r){
		linReg = r;
	}
	public RegressionInfo getLinearRegression(){
		return linReg;
	}

	/*public void update(DailyPrediction prediction){
		//	Fetch and add point data, perform regression on current data set.
		DailyInfo di = prediction.getDailyInfo();
		int curDay = di.getDay();
		//	TODO: Make adNetReport and BidBundle accessible from DailyInfo
		AdxBidBundle bids = di.getBidBundle();
		AdNetworkReport rep = di.getAdNetworkReport();
		Set<AdNetworkKey> keys = rep.keys();
		for(AdNetworkKey k : keys){
			//	Add only points that match this model's key
			//	More efficient to update this outside, working on a collection of PredictionModel
			//	instead of iterating over the report for each key...
			if(!matchesKey(k))
				continue;
			AdNetworkReportEntry temp = rep.getEntry(k);
			double bidPrice = bids.getBid(CostModel.keyToQuery(k));
			addPoint(temp, k.getCampaignId(), bidPrice, curDay);
		}
		//	Grab relevant campaign/key bid-impressions-cost data from prediction or other source
		//	Scope of model TBD - per campaign-key pair?
		updateRegression();
	}*/
	
	/**
	 * Get a predicted y-value from the prediction function
	 * @param x
	 * @return A double representing the predicted y-value for the input x-value
	 */
	public double predict(double x){
		double rv = 0;
		//	Insert x-value into prediction function, get predicted y.
		//	For each element in collection of data (each key-campaign-day?) addPoint to update plot
		return rv;
	}
	
	//	Need to match everything except campaignId
	//	NOT NEEDED if updates are handled from outside the PredictionModel class, working on
	//	a HashTable or HashMap of all PredictionModels
	/*private boolean matchesKey(AdNetworkKey k){
		boolean rv = key.getAdType().equals(k.getAdType()) &&
				key.getDevice().equals(k.getDevice()) &&
				key.getPublisher().equals(k.getPublisher()) &&
				key.getAge().equals(k.getAge()) &&
				key.getGender().equals(k.getGender()) &&
				key.getIncome().equals(k.getIncome());
		return rv;
	}*/
}

/**
 * Holds regression information for a given function - factors and confidence.
 */
class RegressionInfo{
	private double factor1;
	private double factor2;
	private double confidence;
	public double getFactor1(){
		return factor1;
	}
	public void setFactor1(double factor1){
		this.factor1 = factor1;
	}
	public double getFactor2(){
		return factor2;
	}
	public void setFactor2(double factor2){
		this.factor2 = factor2;
	}
	public double getConfidence(){
		return confidence;
	}
	public void setConfidence(double confidence){
		this.confidence = confidence;
	}
}

/*
 * Simple day-stamped data point, containing two doubles and four integers.
 * All fields are final - a ModelEntry is an immutable data point in the graph.
 */
/*class ModelEntry{
	private final int bids;
	private final int wins;
	private final int campaignId;
	private final double cost;
	private final double bidPrice;
	private final int day;	//	Record the day this model
	//	Derived data
	private final double winRate;
	private final double costPerWin;
	
	public ModelEntry(int b, int w, int cid, double c, double p, int day){
		this.bids = b;
		this.wins = w;
		this.campaignId = cid;
		this.cost = c;
		this.bidPrice = p;
		this.day = day;
		//	Derive data
		this.winRate = (double)wins/bids;
		this.costPerWin = cost/wins;
	}

	public int getDay(){
		return day;
	}
	public int getBids(){
		return bids;
	}
	public int getWins(){
		return wins;
	}
	public int getCampaignId(){
		return campaignId;
	}
	public double getCost(){
		return cost;
	}
	public double getBidPrice(){
		return bidPrice;
	}
	public double getWinRate(){
		return winRate;
	}
	public double getCostPerWin(){
		return costPerWin;
	}
}*/