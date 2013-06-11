package brown.tac.adx.models.usermodels;

import brown.tac.adx.predictions.DailyPrediction;

public class UserModel1 extends UserModel {
	public enum UserModelType {
		UM1, UM2, UM3, DEFAULT
	}

	public UserModel1(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void update(DailyPrediction prediction) {
		// TODO Auto-generated method stub

	}

	@Override
	public UserModelOutput getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}
