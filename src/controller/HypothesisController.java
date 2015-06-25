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
import event.ChildUpdateEvent;
import event.ChildUpdateEvent.Request;
import event.ChildUpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 This class will track each HypothesisView and
 *          HypothesisModel and update each accordingly
 */
public class HypothesisController implements ChildUpdateListener {

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

		offX = (int) view.getX();
		offY = (int) view.getY();
		maxWidth = (int) view.getWidth();
		maxHeight = (int) view.getHeight();

		Color[] c = Utilities.colors;

		BayesianModel prh = dataMap.get("prh");
		BayesianView prhV = new BayesianView(offX, offY,
				(int) (maxWidth * prh.getValue()), maxHeight, c[0],
				ScaleDirection.LEFT_RIGHT);
		view.add(prhV);
		childControllers.add(new BayesianController(prhV, prh));

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

		Utilities.sliderListeners.add(this);
	}

	public void handleScaling(BayesianController c) {
		BayesianView view = c.getView();
		BayesianModel model = c.getModel();

		if (view.getScaleDirection() == ScaleDirection.TOP_BOTTOM) {
			view.height = (int) (maxHeight * model.getValue());
		}

		else if (view.getScaleDirection() == ScaleDirection.BOTTOM_TOP) {
			double currentHeight = view.getHeight();
			double newHeight = maxHeight * model.getValue();

			int dy = (int) (newHeight - currentHeight);
			view.y -= dy;
			view.height += dy;
		}

		else if (view.getScaleDirection() == ScaleDirection.LEFT_RIGHT) {
			view.width = (int) (maxWidth * model.getValue());
		}

		else if (view.getScaleDirection() == ScaleDirection.RIGHT_LEFT) {
			double currentWidth = view.getWidth();
			double newWidth = maxWidth * model.getValue();

			int dx = (int) (newWidth - currentWidth);

			view.x -= dx;
			view.width += dx;
		}

		if(model.hasPartners()) {
			for(BayesianModel partner : model.getPartners()) {
				partner.setValue(1.0 - model.getValue());
			}
		}
	}

	public HypothesisView getView() {
		return this.view;
	}
	
	@Override
	public void updateRequest(ChildUpdateEvent e) {
		if (e.getRequest() == Request.VALUE_CHANGE) {
			for (BayesianController c : childControllers) {
				handleScaling(c);
			}
		}
	}
}
