package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import misc.ScaleDirection;
import misc.Utilities;
import model.BayesianModel;
import model.HypothesisModel;
import view.BayesianView;
import view.HypothesisView;

/**
 * @author Jalal
 * @version 6/24/15 This class will track each HypothesisView and
 *          HypothesisModel and update each accordingly
 */
public class HypothesisController {

	private ArrayList<BayesianController> childControllers;

	public HypothesisController(HypothesisView view, HypothesisModel model) {
		childControllers = new ArrayList<BayesianController>();
		setup(view, model);

	}

	/**
	 * Get data through hashmap because the order in which they are added to the
	 * view is important and the map allows us to access the model we want
	 * quickly O(N^2) runtime to search for and get each 
	 * GOD HELP ME 
	 * I only did this because it will be set up this way across every controller, so the
	 * task seemed worthwhile
	 * 
	 * @param view
	 * @param model
	 */
	public void setup(HypothesisView view, HypothesisModel model) {
		HashMap<String, BayesianModel> dataMap = model.getDataMap();

		int offX = (int) view.getX();
		int offY = (int) view.getY();
		int maxWidth = (int) view.getWidth();
		int maxHeight = (int) view.getHeight();

		Color[] c = Utilities.colors;

		BayesianModel prh = dataMap.get("prh");
		BayesianView prhV = new BayesianView(offX, offY,
				(int) (maxWidth * prh.getValue()), maxHeight, c[0],
				ScaleDirection.LEFT_RIGHT);
		view.add(prhV);
		childControllers.add(new BayesianController(prhV, prh));

		BayesianModel prnh = dataMap.get("prnh");
		BayesianView prnhV = new BayesianView((int) (offX + prhV.getWidth()),
				offY, (int) (maxWidth * prnh.getValue()), maxHeight, c[1],
				ScaleDirection.RIGHT_LEFT);
		view.add(prnhV);
		childControllers.add(new BayesianController(prnhV, prnh));

		// within Pr(H)
		BayesianModel preh = dataMap.get("preh");
		BayesianView prehV = new BayesianView(offX, (int) (offY + maxHeight
				* preh.getValue()), (int) prhV.getWidth(), (int) (offY
				+ maxHeight - (offY + maxHeight * preh.getValue())), c[2],
				ScaleDirection.BOTTOM_TOP);
		view.add(prehV);
		childControllers.add(new BayesianController(prehV, preh));

		BayesianModel prneh = dataMap.get("prneh");
		BayesianView prnehV = new BayesianView((int) prehV.getX(), offY,
				(int) prhV.getWidth(), (int) (maxHeight * prneh.getValue()),
				c[3], ScaleDirection.TOP_BOTTOM);
		view.add(prnehV);
		childControllers.add(new BayesianController(prnehV, prneh));

		// within Pr(~H)
		BayesianModel prenh = dataMap.get("prenh");
		BayesianView prenhV = new BayesianView(
				(int) (offX + prhV.getWidth()),
				(int) (offY + maxHeight * prenh.getValue()),
				(int) (prnhV.getWidth()),
				(int) (offY + maxHeight - (offY + maxHeight * prenh.getValue())),
				c[4], ScaleDirection.BOTTOM_TOP);
		view.add(prenhV);
		childControllers.add(new BayesianController(prenhV, prenh));

		BayesianModel prnenh = dataMap.get("prnenh");
		BayesianView prnenhV = new BayesianView((int) prnhV.getX(),
				(int) prnhV.getY(), (int) prnhV.getWidth(),
				(int) (maxHeight * prnenh.getValue()), c[5],
				ScaleDirection.TOP_BOTTOM);
		view.add(prnenhV);
		childControllers.add(new BayesianController(prnenhV, prnenh));

	}
}
