package brown.tac.adx.models.revenue;
import brown.tac.adx.models.Model;

public abstract class RevenueModelForCampaign extends Model{
	public abstract double getRevenueForEffectiveImpressions(double dayStartImps,double alreadyIncrementedImps, double newIncrementEffectiveImps, int campaignId, int day);
}
