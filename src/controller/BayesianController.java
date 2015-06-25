package controller;

import model.BayesianModel;
import view.BayesianView;

/**
 * 
 * @author Jalal
 * @version 6/25/15
 * 
 */
public class BayesianController {

	BayesianView view;
	BayesianModel model;
	
	public BayesianController(BayesianView view, BayesianModel model) {
		this.view = view;
		this.model = model;
	}

	public BayesianView getView() {
		return view;
	}

	public BayesianModel getModel() {
		return model;
	}
	
}
