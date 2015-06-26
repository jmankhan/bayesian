package controller;

import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import misc.ScaleDirection;
import model.BayesianModel;
import view.BayesianControlsView;
import view.BayesianView;

/**
 * This class controls the data for each individual rectangle on the screen. It
 * represents a distinct and singular probability although many of those
 * probabilities are related to each other. This class handles those
 * mathematical relations and is reponsible for each model and view interacting
 * with its related models/views appropriately
 * 
 * @author Jalal
 * @version 6/26/15
 * 
 */
public class BayesianController implements ChangeListener {

	private BayesianView view;
	private BayesianModel model;
	private ArrayList<BayesianController> partners;
	private BayesianControlsView controls;
	private boolean checkPartners;
	private int maxWidth, maxHeight;
	
	public BayesianController(BayesianView view, BayesianModel model) {
		this.view = view;
		this.model = model;
		this.checkPartners = false;

		partners = new ArrayList<BayesianController>();
		setupMax();
		setupControlsView();
	}

	public void setupControlsView() {
		controls = new BayesianControlsView(model.getName(), model.getValue());

		JSlider slider = controls.getSlider();
		slider.addChangeListener(this);
	}

	public void setupMax() {
		if(model.getScaleDirection() == ScaleDirection.LEFT_RIGHT || model.getScaleDirection() == ScaleDirection.RIGHT_LEFT) {
			maxWidth = (int) (view.width/model.getValue());
			maxHeight = view.height;
		}
		else {
			maxWidth = view.width;
			maxHeight = (int) (view.height/model.getValue());
		}
	}
	
	public BayesianView getView() {
		return view;
	}

	public BayesianModel getModel() {
		return model;
	}

	public BayesianControlsView getControls() {
		return this.controls;
	}

	public boolean shouldCheckPartners() {
		return checkPartners;
	}

	public void setCheckPartners(boolean checkPartners) {
		this.checkPartners = checkPartners;
	}

	public void setMaxWidth(int mw) {
		this.maxWidth = mw;
	}
	
	public void setMaxHeight(int mh) {
		this.maxHeight = mh;
	}

	/**
	 * Updates model then view
	 */
	public void update() {
		updateModel();
		updateView();
	}

	/**
	 * Checks partners to update its own values
	 */
	private void updateModel() {

		if (shouldCheckPartners()) { //update partners relative to this model
			int n = partners.size();
			
			for(BayesianController partner:partners) {
				partner.getModel().setValue(n/2 - model.getValue());
				partner.setCheckPartners(false);
				partner.update();
			}
		} else { //update this model relative to partners
			double total = 0;
			for (BayesianController p : partners) {
				total += p.getModel().getValue();
			}

			model.setValue(1.0 - total);
			setCheckPartners(false);
		}
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
			partner.update();
		}
	}

	/**
	 * Scale this controller's view according to its direction
	 * 
	 * @param dir
	 */
	private void scaleView(ScaleDirection dir) {

		if (dir == ScaleDirection.LEFT_RIGHT) {
			view.width = (int) (maxWidth * model.getValue());
			for(BayesianController bc : partners) {
				bc.setMaxWidth(view.width);
				bc.setCheckPartners(false);
				bc.update();
			}
		} else if (dir == ScaleDirection.TOP_BOTTOM) {
			view.height = (int) (maxHeight * model.getValue());
			view.width  = maxWidth;
		} else if (dir == ScaleDirection.RIGHT_LEFT) {
			double currentWidth = view.getWidth();
			double newWidth = maxWidth * model.getValue();
			int dx = (int) (newWidth - currentWidth);

			view.x -= dx;
			view.width += dx;

			for(BayesianController bc : partners) {
				bc.setMaxWidth(view.width);
				bc.setCheckPartners(false);
				bc.update();
			}
			
		} else if (dir == ScaleDirection.BOTTOM_TOP) {
			double currentHeight = view.getHeight();
			double newHeight = maxHeight * model.getValue();
			int dy = (int) (newHeight - currentHeight);

			view.y -= dy;
			view.height += dy;
			view.width = maxWidth;
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		JTextField field = controls.getField();

		double value = slider.getValue() / 100.0;
		field.setText("" + value);

		model.setValue(value);
		setCheckPartners(true);
		update();
	}
}
