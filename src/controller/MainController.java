package controller;

import java.util.ArrayList;

import model.HypothesisModel;
import view.ControlHolderView;
import view.ControlView;
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

		ControlHolderView cView = new ControlHolderView();
		ControlHolderController cController = new ControlHolderController(cView, models);
		
		HypothesisHolderView hView = new HypothesisHolderView();
		HypothesisHolderController  hController = new HypothesisHolderController(hView, models);

		view.add(cView);
		view.add(hView);
		view.pack();
		view.revalidate();
	}
}
