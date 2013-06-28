package brown.tac.adx.models;


import java.util.LinkedList;

//Many things to import for a DOM parser
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.DocumentBuilder; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList; 
import org.w3c.dom.Node; 
import org.w3c.dom.Element; 
import java.io.File; 

import brown.tac.adx.models.usermodels.*;
import brown.tac.adx.predictions.DailyPrediction;

/**
 * Class containing a list of models and updating them in order of dependencies
 */
public class Modeler {
	
	/*
	 * List of models
	 */
	LinkedList<Model> _modelList;
	
	public Modeler(String filename) {
		this.parseModels(filename);
	}

	/* Modifies and returns the DailyPrediction object */
	public DailyPrediction updateModels(DailyPrediction pred) {
		for (Model model : _modelList) {
			// This is meant to alter predictions with each iteration
			// as the models get updated.
			model.update(pred);
		}
		return pred;
	}
	public LinkedList<Model> getModelList(){
		return _modelList;
	}
	/*
	 * Parses the XML file filename and fills in the model list
	 */
	private void parseModels(String filename) {
		/*
//		 * ASSUMPTIONS: XML file is designed as follows: the tagged category is <model>. a model contains 
//		 * attributes of name (currently a string, but may be an enum), and parameters. <name> will be used 
//		 * to decide which model type to instantiate, and <parameter>s will be used in the instantiation
//		 */
//		//naive parser
//		try{
//			File fileToParse = new File(filename); 
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
//			Document document = dBuilder.parse(fileToParse); 
//			
//			NodeList elemList = document.getElementsByTagName("model");
//			for(int i=0; i<elemList.getLength(); i++){
//				Node currNode = elemList.item(i); 
//				if (currNode.getNodeType() == Node.ELEMENT_NODE){
//					Element element = (Element)currNode; 
//					if(element.getAttribute("name").equals("UserModel1")){	//will use enums later
//						//will be modified to efficiently call switch
//						brown.tac.adx.models.usermodels.UserModel1 um1 = new brown.tac.adx.models.usermodels.UserModel1(filename);
//						_modelList.add(um1); 
//				//etc with other explicit model names found in file
//						Model userModel = UserModel.build();
//					
//					}
//				}
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace(); 
//		}
		
	}
	
}
