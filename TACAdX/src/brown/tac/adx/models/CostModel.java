package brown.tac.adx.models;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

import tau.tac.adx.ads.properties.AdType;
import tau.tac.adx.devices.Device;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.AdNetworkKey;
import tau.tac.adx.report.adn.AdNetworkReport;
import tau.tac.adx.report.adn.AdNetworkReportEntry;
import tau.tac.adx.report.adn.MarketSegment;
import tau.tac.adx.users.properties.Age;
import tau.tac.adx.users.properties.Gender;
import tau.tac.adx.users.properties.Income;
import brown.tac.adx.agents.DailyInfo;
import brown.tac.adx.optimizationalgs.Optimizer;
import brown.tac.adx.predictions.DailyPrediction;

/**
 * A Modeler class that manages and encompasses each of the PredictionModel objects that
 * the agent will use for prediction.
 */
public class CostModel /*extends Model*/ implements ModelerInterface{
	//	Stores all initialized models
	Rengine re;
	int nextEntry = 1;
	private HashSet<AdxQuery> modelKeys;
	private HashMap<Integer, Integer> entryCount;
	private HashMap<Integer, Integer> bidCount;
	//	Running average of costPerWin
	private HashMap<Integer, Double> avgCostPerWin;
	private HashMap<AdNetworkKey, Double> bidPrices;
	private AdNetworkReport adNetReport;
	
	
	private Optimizer optimizer;
	//	If DailyInfo is gone, then we need an internal measure of what day it is.
	private int day = 0;

	//	Main function for testing
	public static void main(String[] args){
		//	Create a dummy database entry - does it insert properly?
		CostModel cm = new CostModel();
		System.out.println("CM created");
		AdNetworkKey key = new AdNetworkKey();
		key.setGender(Gender.male);
		key.setAge(Age.Age_18_24);
		key.setIncome(Income.very_high);
		key.setPublisher("asdfasdf");
		key.setAdType(AdType.video);
		key.setDevice(Device.pc);
		AdxQuery q = CostModel.keyToQuery(key);
		cm.newRegression(q);
		cm.addToR(q, 2, 8, 4, 5, 6);
		cm.addToR(q, 3, 9, 5, 6, 7);
		key.setIncome(Income.low);
		key.setGender(Gender.female);
		AdxQuery q1 = CostModel.keyToQuery(key);
		cm.newRegression(q1);
		cm.addToR(q1, 4, 10, 6, 7, 8);
		cm.addToR(q1, 5, 11, 7, 8, 9);
		cm.printDataBase();
		cm.updateRegression(q);
		cm.updateRegression(q1);
		System.out.println("Entries: " + cm.entryCount.get(q.hashCode()) + ", " + cm.entryCount.get(q1.hashCode()));
		System.out.println("Bids: " + cm.bidCount.get(q.hashCode()) + ", " + cm.bidCount.get(q1.hashCode()));
		System.out.println(cm.getBidForImpressions(q, 50));
		System.out.println(cm.getCostForImpressions(q1, 51));
		cm.closeR();
	}

	//	Initialize R and create a new data frame with the required fields!
	public CostModel() {
		System.out.println("Creating Rengine (with arguments)");
		re=new Rengine(new String[0], false, new RCallBackHandler());
		System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}
		//	No keys have been entered yet!
		modelKeys = new HashSet<AdxQuery>();
		entryCount = new HashMap<Integer, Integer>();
		bidCount = new HashMap<Integer, Integer>();
		avgCostPerWin = new HashMap<Integer, Double>();
		//	Initialize our R database stand-in with relevant fields.
		re.eval("costmodel = data.frame(gender = character(0), age = character(0), income = character(0), " +
				"publisher = character(0), adtype = character(0), devicetype = character(0), " +
				"day = integer(0), bids = integer(0), wins = integer(0), cost = numeric(0), " +
				"bidprice = numeric(0), winrate = numeric(0), costperwin = numeric(0), " +
				"hash = integer(0), stringsAsFactors = FALSE)");

	}
	
	/**
	 *  Sets the optimizer to signal when we have the regression data!
	 * @param the Optimizer to set
	 */
	public void setOptimizer(Optimizer o){
		optimizer = o;
	}

	/**
	 * Takes in an AdNetworkKey and relevant information to produce an entry in the R database.
	 * @param key - the AdNetworkKey to use for the database entry
	 * @param day - an integer representing the day the entry was produced
	 * @param bids - an integer number of bids placed for this key on the given day
	 * @param wins - the integer number of bids won for the given key on the given day
	 * @param cost - the total cost of all impression opportunities won for the key-campaign-day
	 * @param bidPrice - the price that was bid for the key-campaign pair on this day
	 */
	public void addToR(AdxQuery q, int day, int bids, int wins, double cost, double bidPrice){
		Set<MarketSegment> mkt = q.getMarketSegments();
		char gender = mkt.contains(MarketSegment.MALE) ? 'm' : 'f';
		char age = mkt.contains(MarketSegment.YOUNG) ? 'y' : 'o';
		char income = mkt.contains(MarketSegment.LOW_INCOME) ? 'l' : 'h';
		String publisher = q.getPublisher();
		String adType = q.getAdType().equals(AdType.text) ? "text" : "video";
		String deviceType = q.getDevice().equals(Device.mobile) ? "mobile" : "desktop";
		//	campaignID should be a field in AdNetworkKey... (not needed?)
		//	int campaignID = 666;//key.getCampaignID();
		double winRate = (double)wins/(double)bids;
		double costPerWin = cost/(double)wins;
		//	Hash an AdxQuery?
		int hash = q.hashCode();
		//re.eval("entrycount_" + hash + " = " + "entrycount_" + hash + " + 1");
		//re.eval("totalbids_" + hash + " = " + "totalbids_" + hash + " + " + bids);
		int entries = entryCount.get(hash);
		entryCount.put(hash, entries + 1);
		bidCount.put(hash, bidCount.get(hash) + bids);
		double newcpw = ((avgCostPerWin.get(hash) * entries) + costPerWin)/(entries + 1);
		avgCostPerWin.put(hash, newcpw);
		
		re.eval("costmodel[" + nextEntry + ",] = c(\"" + gender + "\",\"" + age + "\",\"" + income
				+ "\",\"" + publisher + "\",\"" + adType + "\",\"" + deviceType + "\"," + day + ','
				+ bids + ',' + wins + ',' + cost + ',' + bidPrice + ',' + winRate + ','
				+ costPerWin + ", " + hash + ")");
		System.out.println("Added row: " + nextEntry);
		nextEntry++;
	}
	
	public void updateBids(HashMap<AdNetworkKey, Double> bidPrices){
		this.bidPrices = bidPrices;
		if(adNetReport != null)
			update();
	}
	
	public void updateReport(AdNetworkReport adNetReport){
		this.adNetReport = adNetReport;
		if(bidPrices != null)
			update();
	}

	/* Modifies and returns the DailyPrediction object */
	public void update(/*DailyPrediction pred*/) {
		//		Fetch and add point data, perform regression on current data set.
		//	DailyInfo di = pred.getDailyInfo();
		day++;
		//	int curDay = day;
		//	HashMap<AdNetworkKey, Double> bidPrices = di.getDailyBids();
		//	AdNetworkReport rep = di.getAdNetworkReport();
		Set<AdNetworkKey> keys = adNetReport.keys();
		for(AdNetworkKey k : keys){
			//	Don't need to worry about campaignId because AdxQuery doesn't have a field for it.
			AdxQuery q = CostModel.keyToQuery(k);
			if(!modelKeys.contains(q)){
				modelKeys.add(q);
				newRegression(q);
			}
			AdNetworkReportEntry temp = adNetReport.getEntry(k);
			double bidPrice = bidPrices.get(k);
			int bids = temp.getBidCount();
			int wins = temp.getWinCount();
			double cost = temp.getCost();
			addToR(q, day, bids, wins, cost, bidPrice);
		}
		for(AdxQuery q : modelKeys){
			//	Updates the regression expression for each model
			updateRegression(q);
		}
		//	TODO: Whatever function in Optimizer needs to be called, call it!
		//	optimizer.trigger();
		bidPrices = null;
		adNetReport = null;
	}
	
	//	Sets up a new regression, just 2 variables...
	private void newRegression(AdxQuery q){
		//re.eval("entrycount_" + q.hashCode() + " = 0");
		//re.eval("totalbids_" + q.hashCode() + " = 0");
		entryCount.put(q.hashCode(), 0);
		bidCount.put(q.hashCode(), 0);
		avgCostPerWin.put(q.hashCode(), 0.0);
	}

	private void updateRegression(AdxQuery q){
		//	Subset based on a key's hash code.
		//REXP s1 = re.eval("css = costmodel$hash");
		REXP sub = re.eval("cursubset = subset(costmodel, hash == " + q.hashCode() + ")");
		System.out.println(sub.rtype + ", " + sub.asString());
		printDataBase("cursubset");
		String y1 = "winrate";
		String y2 = "costperwin";
		String x = "bidprice";
		String hash = formatHash(q);
		String name1 = "model_" + x + "_" + y1 + "_" + hash;
		String name2 = "model_" + y2 + "_" + x + "_" + hash;
		System.out.println("CREATING: " + name1 +", " + name2);
		long tt = System.currentTimeMillis();
		re.eval(name1 + " = lm(as.numeric(" + x + ")~as.numeric(" + y1 +
				"), data = cursubset)");//family=binomial(logit), 
		re.eval(name2 + " = lm(as.numeric(" + y2 + ")~as.numeric(" + x + "), data = cursubset)");
		System.out.println(System.currentTimeMillis() - tt);
		
		//	How to get coefficients... needed?
		/*REXP res = re.eval("coefficients(" + name1 + ")");
		double[] coef = res.asDoubleArray();
		//String[] names = nms.asStringArray();
		for(int it = 0; it < coef.length; it++)
			System.out.println(coef[it]);*/
	}

	public void printDataBase(){
		printDataBase("costmodel");
	}

	public void printDataBase(String dfname){
		REXP names = re.eval(dfname);
		//	We can suppress the unchecked Vector conversion because RVector holds REXPs.
		System.out.println("Type: " + names.rtype + names.asString());
		@SuppressWarnings("unchecked")
		Vector<String> nameVector = names.asVector().getNames();
		for(String elem : nameVector){
			System.out.print("\t" + elem);
		}
		System.out.print("\n");
		for(int it = 1; it < nextEntry; it++){
			REXP tab = re.eval(dfname + "[" + it + ",]");
			//	System.out.println("Type: " + tab.rtype + tab.asString());
			//	We can suppress the unchecked Vector conversion because RVector holds REXPs.
			@SuppressWarnings("unchecked")
			Vector<REXP> v = tab.asVector();
			for(REXP elem : v){
				String eprint = elem.getClass().toString() + "    " + elem.rtype;
				switch(elem.rtype){
				case(13) : eprint = "" + elem.asInt();
				case(14) : eprint = "" + elem.asDouble();
				case(16) : eprint = elem.asString();
				}
				System.out.print("\t" + eprint);
			}
			System.out.print("\n");
		}
	}

	@Override
	public double getBidForImpressions(AdxQuery key, double impressions){
		int totalBids = bidCount.get(key.hashCode());
		String modelName = "model_bidprice_winrate_" + formatHash(key);
		double wr = impressions/(double) totalBids;
		REXP prediction = re.eval("predict(" + modelName + ", data.frame(winrate = " + wr + "))");
		double rv = prediction.asDouble();
		System.out.println("RV: " + rv);
		try{
			return rv;
		}
		catch(Exception e){
			//	TODO: WHAT DO IF FAIL?!?!?!?
			return -1;
		}
	}

	@Override
	public double getCostForImpressions(AdxQuery key, double impressions){
		double avg = avgCostPerWin.get(key.hashCode());
		double bp = getBidForImpressions(key, impressions);
		String modelName = "model_costperwin_bidprice_" + formatHash(key);
		REXP prediction = re.eval("predict(" + modelName + ", data.frame(bidprice = " + bp + "))");
		double rv = prediction.asDouble();
		System.out.println("RV: " + rv);
		rv *= impressions;
		if(rv > avg * 1.5){
			rv = avg * 1.5;
		}
		else if(rv < avg * .5){
			rv = avg * .5;
		}
		try{
			if(entryCount.get(key) < 3)
				throw new Exception();
			return rv;
		}
		catch(Exception e){
			return avg;
		}
	}

	@Override
	public double getRevenueForEffectiveImpressions(String campaignID,
			double effectiveImpressions) {
		// Dummy? CostModel doesn't need this...
		return 0;
	}
	
	//	Return query hash as a String
	private String formatHash(AdxQuery q){
		String hash = "" + q.hashCode();
		//	R will interpret a '-' character as minus, not as part of a name - thus FAIL.
		if(q.hashCode() < 0){
			hash = "a" + (-1 * q.hashCode());
		}
		return hash;
	}

	public void closeR(){
		re.end();
	}

	/**
	 * Takes in an AdNetworkKey and produces the corresponding AdxQuery
	 * @param An AdNetworkKey k to be translated to an AdxQuery
	 * @return The AdxQuery that corresponds to k
	 */
	public static AdxQuery keyToQuery(AdNetworkKey k){
		//	Convert the key's attributes into a HashSet of MarketSegments
		HashSet<MarketSegment> mkt = new HashSet<MarketSegment>();
		mkt.add((k.getAge() == Age.Age_18_24 || k.getAge() == Age.Age_25_34
				|| k.getAge() == Age.Age_35_44) ? MarketSegment.YOUNG : MarketSegment.OLD);
		mkt.add((k.getGender() == Gender.male) ? MarketSegment.MALE : MarketSegment.FEMALE);
		mkt.add((k.getIncome() == Income.low || k.getIncome() == Income.medium) ?
				MarketSegment.LOW_INCOME : MarketSegment.HIGH_INCOME);
		AdxQuery rv = new AdxQuery(k.getPublisher(), mkt, k.getDevice(), k.getAdType());
		return rv;
	}

	public static AdNetworkKey queryToKey(AdxQuery q){
		//	Convert the query's MarketSegments HashSet into AdNetworkKey attributes.
		AdNetworkKey rv = new AdNetworkKey();
		Set<MarketSegment> mkt = q.getMarketSegments();
		rv.setGender(mkt.contains(MarketSegment.MALE) ? Gender.male : Gender.female);
		//	Highly inexact, but AdxQuery doesn't have that precision
		rv.setAge(mkt.contains(MarketSegment.YOUNG) ? Age.Age_18_24 : Age.Age_65_PLUS);
		rv.setIncome(mkt.contains(MarketSegment.HIGH_INCOME) ? Income.very_high : Income.low);
		rv.setPublisher(q.getPublisher());
		rv.setAdType(q.getAdType());
		rv.setDevice(q.getDevice());
		return rv;
	}
}

/**
 * RCallBackHandler handles the main loop callbacks for the JRI Rengine used in CostModel
 */
class RCallBackHandler implements RMainLoopCallbacks{

	@Override
	public void rBusy(Rengine re, int which){}

	@Override
	public String rChooseFile(Rengine re, int newFile) {
		FileDialog fd = new FileDialog(new Frame(), (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
		fd.setVisible(true);
		String res=null;
		if (fd.getDirectory()!=null) res=fd.getDirectory();
		if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
		return res;
	}

	@Override
	public void rFlushConsole(Rengine re){}

	@Override
	public void rLoadHistory(Rengine re, String arg1){}

	@Override
	public String rReadConsole(Rengine re, String msg, int addHistory){
		return msg;
	}

	@Override
	public void rSaveHistory(Rengine re, String filename){}

	@Override
	public void rShowMessage(Rengine re, String filename){}

	@Override
	public void rWriteConsole(Rengine re, String text, int oType) {
		System.out.print(text);
	}
}