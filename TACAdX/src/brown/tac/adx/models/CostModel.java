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
import tau.tac.adx.props.AdxBidBundle;
import tau.tac.adx.props.AdxQuery;
import tau.tac.adx.report.adn.AdNetworkKey;
import tau.tac.adx.report.adn.AdNetworkReport;
import tau.tac.adx.report.adn.AdNetworkReportEntry;
import tau.tac.adx.report.adn.MarketSegment;
import tau.tac.adx.users.properties.Age;
import tau.tac.adx.users.properties.Gender;
import tau.tac.adx.users.properties.Income;
import brown.tac.adx.agents.DailyInfo;
import brown.tac.adx.predictions.DailyPrediction;

/**
 * A Modeler class that manages and encompasses each of the PredictionModel objects that
 * the agent will use for prediction.
 */
public class CostModel extends Model {
	//	Stores all initialized models
	Rengine re;
	int nextEntry = 1;
	private HashMap<AdNetworkKey, CostModelEntry> _modelTable;

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
		cm.addToR(key, 2, 8, 4, 5, 6);
		cm.addToR(key, 3, 9, 5, 6, 7);
		key.setIncome(Income.low);
		key.setGender(Gender.female);
		cm.addToR(key, 4, 10, 6, 7, 8);
		cm.addToR(key, 5, 11, 7, 8, 9);
		cm.printDataBase();
		CostModelEntry cme = new CostModelEntry(key);
		cm.updateRegression(cme);
		cm.closeR();
	}

	//	Modeler's constructor is good enough.
	public CostModel() {
		System.out.println("Creating Rengine (with arguments)");
		re=new Rengine(new String[0], false, new TextConsole());
		System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			return;
		}
		//	Initialize our R database stand-in with 14 fields.
		//	TODO: Figure out if it's better to store all key components or to convert each
		//	AdNetworkKey to a single string and use that.
		re.eval("costmodel = data.frame(gender = character(0), age = character(0), income = character(0), " +
				"publisher = character(0), adtype = character(0), devicetype = character(0), " +
				"day = integer(0), bids = integer(0), wins = integer(0), campaignid = integer(0), " +
				"cost = numeric(0), bidprice = numeric(0), winrate = numeric(0), " +
				"costperwin = numeric(0), stringsAsFactors = FALSE)");

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
	public void addToR(AdNetworkKey key, int day, int bids, int wins, double cost, double bidPrice){
		char gender = key.getGender().equals(Gender.male) ? 'm' : 'f';
		char age = (key.getAge().equals(Age.Age_18_24) || key.getAge().equals(Age.Age_25_34)
				|| key.getAge().equals(Age.Age_35_44)) ? 'y' : 'o';
		char income = (key.getIncome().equals(Income.low) || key.getIncome().equals(Income.medium))
				? 'l' : 'h';
		String publisher = key.getPublisher();
		String adType = key.getAdType().equals(AdType.text) ? "text" : "video";
		String deviceType = key.getDevice().equals(Device.mobile) ? "mobile" : "desktop";
		//	campaignID should be a field in AdNetworkKey...
		int campaignID = 666;//key.getCampaignID();
		double winRate = (double)wins/(double)bids;
		double costPerWin = cost/(double)wins;
		re.eval("costmodel[" + nextEntry + ",] = c(\"" + gender + "\",\"" + age + "\",\"" + income
				+ "\",\"" + publisher + "\",\"" + adType + "\",\"" + deviceType + "\"," + day + ','
				+ bids + ',' + wins + ',' + campaignID + ',' + cost + ',' + bidPrice + ',' + winRate
				+ ',' + costPerWin + ")");
		System.out.println("Added row: " + nextEntry);
		nextEntry++;
	}

	/* Modifies and returns the DailyPrediction object */
	public void update(DailyPrediction pred) {
		//		Fetch and add point data, perform regression on current data set.
		DailyInfo di = pred.getDailyInfo();
		int curDay = di.getDay();
		//	TODO: Make adNetReport and BidBundle accessible from DailyInfo
		AdxBidBundle bidbundle = di.getBidBundle();
		AdNetworkReport rep = di.getAdNetworkReport();
		Set<AdNetworkKey> keys = rep.keys();
		for(AdNetworkKey k : keys){
			int cid = k.getCampaignId();
			k.setCampaignId(0);
			//	Match key to model - if nonexistent, create new model...
			if(!_modelTable.containsKey(k)){
				CostModelEntry pm = new CostModelEntry(k);
				_modelTable.put(k, pm);
			}
			k.setCampaignId(cid);
			AdNetworkReportEntry temp = rep.getEntry(k);
			double bidPrice = bidbundle.getBid(keyToQuery(k));
			int bids = temp.getBidCount();
			int wins = temp.getWinCount();
			double cost = temp.getCost();
			addToR(k, curDay, bids, wins, cost, bidPrice);
		}
		for(CostModelEntry pm : _modelTable.values()){
			//	Updates the regression expression for each model
			updateRegression(pm);
		}
	}

	public void updateRegression(CostModelEntry cme){
		//	Subset the database - maybe an easier way to do this?
		AdNetworkKey key = cme.getKey();
		String g = "gender ==" + (key.getGender().equals(Gender.male) ? "\"m\"" : "\"f\"");
		String a = "age ==" + ((key.getAge().equals(Age.Age_18_24) ||
				key.getAge().equals(Age.Age_25_34) || key.getAge().equals(Age.Age_35_44)) ? "\"y\"" : "\"o\"");
		String i = "income ==" + ((key.getIncome().equals(Income.low) ||
				key.getIncome().equals(Income.medium)) ? "\"l\"" : "\"h\"");
		String p = "publisher ==\"" + key.getPublisher() + "\"";
		String at = "adtype ==" + (key.getAdType().equals(AdType.text) ? "\"text\"" : "\"video\"");
		String d = "devicetype ==" + (key.getDevice().equals(Device.mobile) ? "\"mobile\"" : "\"desktop\"");
		REXP sub = re.eval("cursubset = subset(costmodel, " + g + "&" + a + "&" + i + "&" + p + "&" + at + "&" + d + ")");
		System.out.println(sub.rtype + ", " + sub.asString());
		printDataBase("cursubset");
		String y1 = "winrate";
		String y2 = "costperwin";
		String x = "bidprice";
		re.eval("curmodel = (lm(cursubset$" + y1 + "~cursubset$" + x + "))");
		REXP res = re.eval("coefficients(curmodel)");
		//REXP nms = re.eval("names(curmodel)");
		double[] coef = res.asDoubleArray();
		//String[] names = nms.asStringArray();
		for(int it = 0; it < coef.length; it++)
			System.out.println(coef[it]);
		
		/*re.eval("a = c(14, 15, 18, 28)");
		re.eval("b = c(16, 19, 22, 32)");
		res = re.eval("lm(a~b)");*/
		//	We can suppress the unchecked Vector conversion because RVector holds REXPs.
		/*@SuppressWarnings("unchecked")
		Vector<REXP> vec = res.asVector();
		System.out.println("VECSIZE: " + vec.size());*/
		/*for(REXP elem : vec){
			String eprint = ""+elem.rtype;
			if(elem.rtype == 13){
				eprint += ", " + elem.asInt();
			}
			if(elem.rtype == 14){
				eprint += ", " + elem.asDouble();
			}
			if(elem.rtype == 16){
				eprint += ", " + elem.asString();
			}
			if(elem.rtype == 19){
				if(elem == null)
					eprint = "NULL"	;
				else if(elem.asDoubleArray() != null){
					eprint = "Array";
					for(double dub : elem.asDoubleArray())
						eprint += ", " + dub;
					}
				else if(elem.asStringArray() != null){
					eprint = "StrArray:";
					for(String str : elem.asStringArray()){
						eprint += ", " + str;
					}
				}
				else{
					eprint = "Vector:";
					for(Object obj : elem.asVector()){
						if(obj instanceof REXP){
							switch(((REXP) obj).rtype){
								case(6) : eprint += "STRING-" + elem.asString() + ", BOOL-" + elem.asBool();
								case(13) : eprint += ", " + ((REXP) obj).rtype + "/" + elem.asInt();
								case(14) : eprint += ", " + ((REXP) obj).rtype + "/" + elem.asDouble();
								case(16) : eprint += ", " + ((REXP) obj).rtype + "/" + elem.asString();
							}
						}
						else
							eprint += ", " + obj.getClass().toString();
					}
				}
			}
			System.out.print("\n" + eprint);
		}
		System.out.print("\n");*/
	}

	public void printDataBase(){
		printDataBase("costmodel");
	}

	public void printDataBase(String dfname){
		REXP names = re.eval(dfname);
		//	We can suppress the unchecked Vector conversion because RVector holds REXPs.
		@SuppressWarnings("unchecked")
		Vector<String> nameVector = names.asVector().getNames();
		System.out.println("Type: " + names.rtype + names.asString());
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