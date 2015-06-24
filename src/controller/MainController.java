package controller;

import java.util.ArrayList;

import misc.Utilities;
import model.HypothesisModel;
import view.HypothesisHolderView;
import view.MainView;

/**
 * 
 * @author Jalal
 * @version 6/24/15
 * Entry point
 * Instantiates MainView and the other controllers, passing them their views and data
 */
public class MainController {

	public static void main(String args[]) {
		MainView view = new MainView();
		
		//add one hypothesis to h.holder
		ArrayList<HypothesisModel> models = new ArrayList<HypothesisModel>();
		models.add(new HypothesisModel());
		
		HypothesisHolderView vView = new HypothesisHolderView();
		HypothesisHolderController  vController = new HypothesisHolderController(vView, models);
		
		view.add(vView);
		view.revalidate();
	}
}
