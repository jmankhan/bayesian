package controller;

import java.util.ArrayList;

import misc.ScaleDirection;
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
	private ArrayList<BayesianController> partners;

	public BayesianController(BayesianView view, BayesianModel model) {
		this.view = view;
		this.model = model;
		
		partners = new ArrayList<BayesianController>();
	}

	public BayesianView getView() {
		return view;
	}

	public BayesianModel getModel() {
		return model;
	}

	/**
	 * Updates model then view
	 */
	public void update() {
		updateModel();
		updateView();
		updatePartners();
	}
	
	/**
	 * Checks partners to update its own values
	 */
	private void updateModel() {
		double total = 0;
		for(BayesianController p : partners) {
			total += p.getModel().getValue();
		}
		
		//if controller has no partners, then don't update by relative values
		if(total != 0.0)
			model.setValue(1.0-(total));
	}
	
	/**
	 * Checks model for information and updates views
	 */
	private void updateView() {
		scaleView(model.getScaleDirection());
	}

	public void setPartners(ArrayList<BayesianController> partners) {
		this.partners = partners;
	}

	public void addPartner(BayesianController partner) {
		this.partners.add(partner);
	}
	
	/**
	 * Calls the update method for each BayesianModel that is assigned as a
	 * partner to this controller
	 */
	public void updatePartners() {
		for (BayesianController partner : partners) {
			partner.updateView();
		}
	}

	/**
	 * Scale this controller's view according to its direction
	 * 
	 * @param dir
	 */
	public void scaleView(ScaleDirection dir) {
		if (dir == ScaleDirection.LEFT_RIGHT) {
			view.width = (int) (BayesianModel.MAX_WIDTH * model.getValue());
		} 
		else if (dir == ScaleDirection.TOP_BOTTOM) {
			view.height = (int) (BayesianModel.MAX_HEIGHT * model.getValue());
		} 
		else if (dir == ScaleDirection.RIGHT_LEFT) {
			double currentWidth = view.getWidth();
			double newWidth = BayesianModel.MAX_WIDTH * model.getValue();
			int dx = (int) (newWidth - currentWidth);

			view.x -= dx;
			view.width += dx;
			
		} 
		else if (dir == ScaleDirection.BOTTOM_TOP) {
			double currentHeight = view.getHeight();
			double newHeight = BayesianModel.MAX_HEIGHT * model.getValue();
			int dy = (int) (newHeight - currentHeight);

			view.y -= dy;
			view.height += dy;
		}
	}
}
