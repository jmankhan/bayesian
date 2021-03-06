package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import misc.Utilities;
import model.BayesianModel;
import model.HypothesisModel;
import view.BayesianControlsView;
import view.BayesianView;
import view.HypothesisView;

/**
 * This class will track each HypothesisView and HypothesisModel and update each accordingly
 * @author Jalal
 * @version 6/26/15 
 */
public class HypothesisController {

	private ArrayList<BayesianController> childControllers;
	private ArrayList<BayesianControlsView> controlsViews;

	private int offX, offY, maxWidth, maxHeight;
	private HypothesisView view;
	private HypothesisModel model;

	public HypothesisController(HypothesisView view, HypothesisModel model) {

		this.view = view;
		this.model = model;
		setup();

	}

	/**
	 * Get data through hashmap because the order in which they are added to the
	 * view is important and the map allows us to access the model we want
	 * Although the runtime complexity is O(N^2), there is a small sample, so it
	 * should not impact performance too heavily
	 * 
	 * @param view
	 * @param model
	 */
	public void setup() {

		setupChildControllers();
		setupControlsView();
	}

	public void setupChildControllers() {
		childControllers = new ArrayList<BayesianController>();
		HashMap<String, BayesianModel> dataMap = model.getDataMap();

		// setup variables used to determine view placement
		offX = (int) view.getX();
		offY = (int) view.getY();
		maxWidth = Utilities.cellSize * 5;
		maxHeight = Utilities.cellSize * 5;

		Color[] c = Utilities.colors;

		// create views based on model information
		BayesianModel prh = dataMap.get("prh");
		BayesianView prhV = new BayesianView(offX, offY,
				(int) (maxWidth * prh.getValue()), maxHeight, c[0]);
		view.add(prhV);
		BayesianController prhC = new BayesianController(prhV, prh);

		BayesianModel preh = dataMap.get("preh");
		BayesianView prehV = new BayesianView(
				offX,
				(int) (offY + maxHeight * preh.getValue()),
				prhV.width,
				(int) (offY + maxHeight - (offY + maxHeight * preh.getValue())),
				c[1]);
		view.add(prehV);
		BayesianController prehC = new BayesianController(prehV, preh);

		BayesianModel prneh = dataMap.get("prneh");
		BayesianView prnehV = new BayesianView(prhV.x, offY, prhV.width,
				(int) (maxHeight * prneh.getValue()), c[2]);
		view.add(prnehV);
		BayesianController prnehC = new BayesianController(prnehV, prneh);

		// assign partners
		prnehC.addPartner(prehC);
		prehC.addPartner(prnehC);
		prhC.addPartner(prehC);
		prhC.addPartner(prnehC);
		
		// add all to childControllers
		childControllers.add(prhC);
		childControllers.add(prehC);
		childControllers.add(prnehC);

	}

	public void setupControlsView() {
		controlsViews = new ArrayList<BayesianControlsView>();
		for (BayesianController bc : childControllers) {
			controlsViews.add(bc.getControls());
		}
	}

	public HypothesisView getView() {
		return this.view;
	}

	public HypothesisModel getModel() {
		return this.model;
	}

	public ArrayList<BayesianController> getChildControllers() {
		return this.childControllers;
	}
	
	public ArrayList<BayesianControlsView> getControlsViews() {
		return this.controlsViews;
	}
}
