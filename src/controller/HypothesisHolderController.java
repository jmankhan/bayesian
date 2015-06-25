package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;

import misc.Utilities;
import model.HypothesisModel;
import view.BayesianControlsView;
import view.BayesianView;
import view.HypothesisControlsView;
import view.HypothesisHolderView;
import view.HypothesisView;
import event.UpdateEvent;
import event.UpdateEvent.Request;
import event.UpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 This controller will update the HypothesisHolderView but
 *          will not directly issue commands It will also instantiate the
 *          HypothesisControllers that belong to it and pass them their views
 *          and data It will receive events from them and pass them along
 * 
 *          TODO:fix second mouse click on same target
 */
public class HypothesisHolderController implements UpdateListener, ActionListener {

	private HypothesisHolderView view;
	private ArrayList<HypothesisController> childHControllers;
	private ArrayList<HypothesisModel> models;
	private HypothesisControlsView hcv;
	
	public HypothesisHolderController(HypothesisHolderView view,
			ArrayList<HypothesisModel> models) {
		Utilities.hypothesisListeners.add(this);
		this.view = view;
		this.models = models;
		setup();
	}

	public void setup() {

		setupChildHypothesisControllers();
		setupControls();
		
		// add mouselistener
		BayesianMouseAdapter adapter = new BayesianMouseAdapter();
		view.addMouseListener(adapter);
		view.addMouseMotionListener(adapter);
	}

	public void setupChildHypothesisControllers() {
		childHControllers = new ArrayList<HypothesisController>();
		
		// prepare view array
		HypothesisView[] hViews = new HypothesisView[models.size()];

		// initialize constants to place each view, attempt to make is square-ish
		int offX = Utilities.prefSize.width / HypothesisModel.hypotheses;
		int offY = Utilities.prefSize.height / 4;
		int height = Utilities.prefSize.height / 2;
		int width = Math.min(offX, height);

		// add each child view horizontally to this view and pass it to its
		// controller
		for (int i = 0; i < hViews.length; i++) {
			HypothesisView hView = new HypothesisView(offX, offY, width, height);
			offX += width;

			childHControllers
					.add(new HypothesisController(hView, models.get(i)));
			view.add(hView);
		}
		
	}

	public void setupControls() {
		for(HypothesisController hc : childHControllers) {
			for(BayesianControlsView bcv : hc.getControlsViews()) {
				view.addBCV(bcv);
			}
		}
		
		hcv = new HypothesisControlsView();
		hcv.getButton().addActionListener(this);;
		hcv.getCombo().addActionListener(this);;
		
		view.add(hcv);
	}

	/**
	 * Creates a new Hypothesis MVC, adds view to this view, adds controller to child controllers
	 * @param model
	 */
	public void addNewHypothesis(HypothesisModel model) {

		HypothesisView last = childHControllers.get(childHControllers.size() - 1)
				.getView();
		int offX = last.x + last.width;
		int offY = last.y;
		int width = last.width;
		int height = last.height;

		HypothesisView view = new HypothesisView(offX, offY, width, height);

		this.view.add(view);
		childHControllers.add(new HypothesisController(view, model));
	}

	@Override
	public void updateRequest(UpdateEvent e) {
		if (e.getRequest() == Request.NEW_HYPOTHESIS)
			addNewHypothesis(e.getHypothesisModel());
	}

	class BayesianMouseAdapter extends MouseAdapter {

		private Point start;
		private BayesianView target;
		private int dx, dy;

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			start = e.getPoint();

			HypothesisHolderView hhv = (HypothesisHolderView) e.getSource();
			for (HypothesisView hv : hhv.getChildren()) {
				// go backwards so the top level view gets selected first
				// in case of overlap
				for (int i = hv.getChildren().size() - 1; i >= 0; i--) {
					BayesianView bv = hv.getChildren().get(i);

					if (bv.contains(start)) {
						target = bv;
						dx = (int) (e.getX() - target.getX());
						dy = (int) (e.getY() - target.getY());

						return;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);

			if (target != null) {
				target.setLocation((int) (e.getX() - dx), (int) (e.getY() - dy));
				target.updateListener(new UpdateEvent(Request.REPAINT));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			if (target != null) {
				target.setLocation(
						(int) (Utilities.cellSize * Math.round(target.getX()
								/ Utilities.cellSize)),
						(int) (Utilities.cellSize * Math.round(target.getY()
								/ Utilities.cellSize)));
			}
			target = null;
			start = null;
			dx = 0;
			dy = 0;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() instanceof JButton) {
			
			//create new hypothesis
			addNewHypothesis(new HypothesisModel());
			
			//add new entry to combo
			JComboBox<String> box = hcv.getCombo();
			box.addItem("Hypothesis " + (HypothesisModel.hypotheses-1));
		}
		
		else if(e.getSource() instanceof JComboBox) {
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			int index = box.getSelectedIndex();

			view.removeBCV();
			for(BayesianControlsView bcv : childHControllers.get(index).getControlsViews()) {
				view.addBCV(bcv);
			}
			view.revalidate();
		}
	}
}