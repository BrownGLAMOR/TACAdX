package brown.tac.adx.models.usermodels;

import java.util.List;
import java.util.LinkedList;

import brown.tac.adx.models.TestDailyPredictions;

public class UserModelPerformanceTest {
	
	public UserModelPerformanceTest() {
		// Set up ground truth values for the tests
	}

	public void compareAllModels(String filename) {
		LinkedList<UserModel> models = new LinkedList<UserModel>();
		for (UserModel.UserModelType type : UserModel.UserModelType.values()) {
			if (type == UserModel.UserModelType.DEFAULT) {
				continue;
			}
			models.add(UserModel.build(type, filename));
		}
		this.runComparison(models);
	}
	
	public void compareModels(String filename, UserModel.UserModelType m1, UserModel.UserModelType m2) {
		LinkedList<UserModel> models = new LinkedList<UserModel>();
		models.add(UserModel.build(m1, filename));
		models.add(UserModel.build(m2, filename));
		this.runComparison(models);
	}
	
	public void compareModelList(String filename, List<UserModel.UserModelType> types) {
		LinkedList<UserModel> models = new LinkedList<UserModel>();
		for (UserModel.UserModelType type : types) {
			models.add(UserModel.build(type, filename));
		}
		this.runComparison(models);
	}
	
	private void runComparison(LinkedList<UserModel> models) {
		/*
		 * Create TestDailyPredictions object with stubbed dependencies and phony daily info
		 */	
		for (UserModel model : models) {
			//TODO: Don't pass null
			TestDailyPredictions preds = new TestDailyPredictions(null);
			model.update(preds);
			// Calculate and print error or other statistics in another method
			this.analyzeResults(model.getOutput());
		}
		
	}
	
	private void analyzeResults(UserModelOutput out) {
		
	}
	
	
	public static void main(String[] args) {
		UserModelPerformanceTest test = new UserModelPerformanceTest();
		test.compareModels("", UserModel.UserModelType.UM1, UserModel.UserModelType.UM3);
		test.compareAllModels("");
	}
	
	
}
