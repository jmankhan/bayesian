package controller;

import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import misc.ScaleDirection;
import model.BayesianModel;
import model.HypothesisModel;
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
public class BayesianController {

	private BayesianView view;
	private BayesianModel model;
	private BayesianControlsView controls;
	private boolean checkPartners;
	private int maxWidth, maxHeight;

	/**
	 * List of controllers that will be impacted within the same hypothesis as
	 * this
	 */
	private ArrayList<BayesianController> partners;

	/**
	 * List of controllers that will be impacted outside of this hypothesis
	 */
	private ArrayList<BayesianController> peers;

	public BayesianController(BayesianView view, BayesianModel model) {
		this.view = view;
		this.model = model;
		this.checkPartners = false;

		partners = new ArrayList<BayesianController>();
		peers = new ArrayList<BayesianController>();

		setupMax();
		setupControlsView();
	}

	private void setupControlsView() {
		controls = new BayesianControlsView(model.getName(), model.getValue());
	}

	private void setupMax() {
		if (model.getScaleDirection() == ScaleDirection.LEFT_RIGHT
				|| model.getScaleDirection() == ScaleDirection.RIGHT_LEFT) {
			maxWidth = (int) (view.width / model.getValue());
			maxHeight = view.height;
		} else {
			maxWidth = view.width;
			maxHeight = (int) (view.height / model.getValue());
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

	public ArrayList<BayesianController> getPeers() {
		return this.peers;
	}

	public void addChangeListener(ChangeListener cl) {
		controls.getSlider().addChangeListener(cl);
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
	 * Checks if the parameter is already included in the partner list
	 * 
	 * @param bc
	 * @return
	 */
	public boolean isPartnered(BayesianController bc) {
		for (BayesianController partner : partners) {
			if (partner.equals(bc))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the parameter is already included in the peer list
	 * 
	 * @param bc
	 * @return
	 */
	public boolean isPeered(BayesianController bc) {
		for (BayesianController peer : peers) {
			if (peer.equals(bc))
				return true;
		}
		return false;
	}

	/**
	 * Flag signalling if the controller should update based on its partners
	 * values or its given value
	 * 
	 * @return
	 */
	public boolean shouldCheckPartners() {
		return checkPartners;
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

		if (shouldCheckPartners()) { // update partners relative to this model
			int n = partners.size();

			for (BayesianController partner : partners) {
				partner.getModel().setValue(n / 2 - model.getValue());
				partner.setCheckPartners(false);
				partner.update();
			}
		} else { // update this model relative to partners
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
	public void updateView() {
		scaleView(model.getScaleDirection());
	}

	/**
	 * Adds a new partner to this controller's list of partners. Checks if it is
	 * already in the list, in which case it is not added again
	 * 
	 * @param partner
	 */
	public void addPartner(BayesianController partner) {
		if (!this.isPartnered(partner))
			this.partners.add(partner);
	}

	/**
	 * Adds a new peer to this controller's list of peers. Checks if the peer is
	 * already in the list, in which case it is not added again
	 * 
	 * @param peer
	 */
	public void addPeer(BayesianController peer) {
		this.peers.add(peer);
	}

	/**
	 * Scale this controller's view according to its direction
	 * 
	 * @param dir
	 */
	private void scaleView(ScaleDirection dir) {

		if (dir == ScaleDirection.LEFT_RIGHT) {
			view.width = (int) (maxWidth * model.getValue());

			// update partners
			for (BayesianController bc : partners) {
				bc.setMaxWidth(view.width);
				bc.setCheckPartners(false);
				bc.update();
			}

		} else if (dir == ScaleDirection.TOP_BOTTOM) {
			view.height = (int) (maxHeight * model.getValue());
			view.width = maxWidth;
		} else if (dir == ScaleDirection.RIGHT_LEFT) {
			double currentWidth = view.getWidth();
			double newWidth = maxWidth * model.getValue();
			int dx = (int) (newWidth - currentWidth);

			view.x -= dx;
			view.width += dx;

			for (BayesianController bc : partners) {
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
}
