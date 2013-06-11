package brown.tac.adx.predictions;

import brown.tac.adx.agents.DailyInfo;
import brown.tac.adx.models.Model;
import brown.tac.adx.models.usermodels.UserModel;

public class DailyPrediction {
	
	DailyInfo _info;
	UserModel _userModel;
	
	public DailyPrediction(DailyInfo info) {
		_info = info;
	}
	
	public UserModelPrediction getUserModelPrediction() {
		if (_userModel == null) {
			try {
				throw new Exception("User Model Undefined");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _userModel.getPrediction();
	}
	
	public DailyInfo getDailyInfo() {
		return _info;
	}
	
	public void setModel(Model m) {
		
	}
	
}
