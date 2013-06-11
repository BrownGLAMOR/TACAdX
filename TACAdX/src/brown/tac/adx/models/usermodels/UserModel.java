package brown.tac.adx.models.usermodels;

import brown.tac.adx.models.Model;
import brown.tac.adx.predictions.UserModelPrediction;

public abstract class UserModel extends Model {
	
	public enum UserModelType {
		UM1, UM2, UM3, DEFAULT
	}
	 
	public static UserModel build() {
		return build(UserModelType.DEFAULT, "");
	}
	
	public static UserModel build(UserModelType type, String filename) {
		switch (type) {
		case UM1:
			return new UserModel1(filename);
		case UM2:
			//return new UserModel2(filename);
			return new UserModel1(filename);
		case UM3:
			//return new UserModel3(filename);
			return new UserModel1(filename);
		case DEFAULT:
			//return new UserModel2(filename);
			return new UserModel1(filename);
			
		}
		return new UserModel1(filename);
		
	}
	
	public UserModel(String filename) {
		this.setDefaultParameters();
		this.setParameters(filename);
	}
	
	public void setDefaultParameters() {
		
	}
	
	/* Override this method for algorithm specific
	 * parameters, make sure to call super()
	 */
	public void setParameters(String filename) {
		// Parse XML file for parameter settings
	}
	
	public abstract UserModelOutput getOutput();

	public UserModelPrediction getPrediction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
