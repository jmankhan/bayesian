package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import misc.Utilities;
import model.BayesianModel;
import model.HypothesisModel;
import view.BayesianView;
import view.HypothesisView;
import event.UpdateEvent;
import event.UpdateEvent.Request;
import event.UpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 This class will track each HypothesisView and
 *          HypothesisModel and update each accordingly
 */
public class HypothesisController implements UpdateListener {

	private ArrayList<BayesianController> childControllers;
	private int offX, offY, maxWidth, maxHeight;
	private HypothesisView view;

	public HypothesisController(HypothesisView view, HypothesisModel model) {

		this.view = view;
		childControllers = new ArrayList<BayesianController>();
		setup(view, model);

	}

	/**
	 * Get data through hashmap because the order in which they are added to the
	 * view is important and the map allows us to access the model we want
	 * quickly O(N^2) runtime to search for and get each GOD HELP ME I only did
	 * this because it will be set up this way across every controller, so the
	 * task seemed worthwhile
	 * 
	 * @param view
	 * @param model
	 */
	public void setup(HypothesisView view, HypothesisModel model) {
		HashMap<String, BayesianModel> dataMap = model.getDataMap();

		//setup variables used to determine view placement
		offX = (int) view.getX();
		offY = (int) view.getY();
		maxWidth = (int) view.getWidth();
		maxHeight = (int) view.getHeight();

		Color[] c = Utilities.colors;

		BayesianModel.MAX_WIDTH = maxWidth;
		BayesianModel.MAX_HEIGHT = maxHeight;
		
		//create views based on model information
		BayesianModel prh = dataMap.get("prh");
		BayesianView prhV = new BayesianView(offX, offY,
				(int) (maxWidth * prh.getValue()), maxHeight, c[0]);
		view.add(prhV);
		BayesianController prhC = new BayesianController(prhV, prh);

		BayesianModel preh = dataMap.get("preh");
		BayesianView prehV = new BayesianView(offX, (int) (offY + maxHeight
				* preh.getValue()), (int) prhV.getWidth(), (int) (offY
				+ maxHeight - (offY + maxHeight * preh.getValue())), c[1]);
		view.add(prehV);
		BayesianController prehC = new BayesianController(prehV, preh);

		BayesianModel prneh = dataMap.get("prneh");
		BayesianView prnehV = new BayesianView(offX, offY,
				prhV.width, (int) (maxHeight * prneh.getValue()),
				c[2]);
		view.add(prnehV);
		BayesianController prnehC = new BayesianController(prnehV, prneh);

		//assign partners
		prnehC.addPartner(prehC);
		prehC.addPartner(prnehC);
		
		//add all to childControllers
		childControllers.add(prhC);
		childControllers.add(prehC);
		childControllers.add(prnehC);
		
		//add this to global slider listeners
		Utilities.sliderListeners.add(this);
	}

	public HypothesisView getView() {
		return this.view;
	}

	@Override
	public void updateRequest(UpdateEvent e) {
		if (e.getRequest() == Request.VALUE_CHANGE) {
			for (BayesianController c : childControllers) {
				c.update();
			}
		}
	}
}
