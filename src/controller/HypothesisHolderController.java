package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import misc.Utilities;
import model.HypothesisModel;
import view.BayesianControlsView;
import view.HypothesisControlsView;
import view.HypothesisHolderView;
import view.HypothesisView;

/**
 * This controller will update the HypothesisHolderView but will not directly
 * issue commands It will also instantiate the HypothesisControllers that belong
 * to it and pass them their views and data It will receive events from them and
 * pass them along
 * 
 * @author Jalal
 * @version 6/24/15
 * 
 */
public class HypothesisHolderController implements ActionListener {

	private HypothesisHolderView view;
	private ArrayList<HypothesisController> childHControllers;
	private ArrayList<HypothesisModel> models;
	private HypothesisControlsView hcv;

	public HypothesisHolderController(HypothesisHolderView view,
			ArrayList<HypothesisModel> models) {
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

		// initialize constants to place each view, attempt to make is
		// square-ish
		int offX = Utilities.prefSize.width / HypothesisModel.hypotheses;
		int offY = Utilities.prefSize.height / 4;
		int height = Utilities.prefSize.height / 2;
		int width = Math.min(offX, height);

		// add each child view horizontally to this view and pass it to its
		// controller
		for (int i = 0; i < models.size(); i++) {
			HypothesisView hView = new HypothesisView(offX, offY, width, height);
			offX += width;

			childHControllers
					.add(new HypothesisController(hView, models.get(i)));
			view.add(hView);
		}
	}

	public void setupControls() {
		for (HypothesisController hc : childHControllers) {
			for (BayesianController bc : hc.getChildControllers()) {
				bc.addChangeListener(new BayesianChangeListener(bc));

				view.addBCV(bc.getControls());
			}
		}

		hcv = new HypothesisControlsView();
		hcv.getButton().addActionListener(this);
		hcv.getCombo().addActionListener(this);

		view.add(hcv);
	}

	/**
	 * Updates each peer model then view
	 */
	public void updatePeers() {
		// associate with peers, collect all values
		double total = 0.0;
		for (HypothesisController hc : childHControllers) {
			hc.getChildControllers().get(0)
					.addPeer(hc.getChildControllers().get(0));
			total += hc.getChildControllers().get(0).getModel().getValue();
		}

		// update each peer based on collected total
		for (HypothesisController hc : childHControllers) {
			BayesianController bc = hc.getChildControllers().get(0);
			bc.getModel().setValue(bc.getModel().getValue() / total);
			bc.updateView();
		}

	}

	/**
	 * Creates a new Hypothesis MVC, adds view to this view, adds controller to
	 * child controllers
	 * 
	 * @param model
	 */
	public void addNewHypothesis(HypothesisModel model) {

		// setup view
		HypothesisView last = childHControllers.get(
				childHControllers.size() - 1).getView();

		// may change starting location
		int offX = last.x + last.width / 2;
		int offY = last.y;
		int width = last.width;
		int height = last.height;

		HypothesisView view = new HypothesisView(offX, offY, width, height);

		// add view to viewholder
		this.view.add(view);

		// setup controller
		HypothesisController newhc = new HypothesisController(view, model);

		updatePeers();

		childHControllers.add(newhc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JButton) {

			// create new hypothesis
			addNewHypothesis(new HypothesisModel());

			// add new entry to combo
			JComboBox<String> box = hcv.getCombo();
			box.addItem("Hypothesis " + (HypothesisModel.hypotheses - 1));
			view.repaint();
		}

		else if (e.getSource() instanceof JComboBox) {
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			int index = box.getSelectedIndex();

			view.removeBCV();
			for (BayesianController bc : childHControllers.get(index)
					.getChildControllers()) {
				bc.addChangeListener(new BayesianChangeListener(bc));
				view.addBCV(bc.getControls());
			}
			view.revalidate();
		}
	}

	class BayesianMouseAdapter extends MouseAdapter {

		private Point start;
		private BayesianController target;
		private int dx, dy;

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			start = e.getPoint();

			for (HypothesisController hc : childHControllers) {
				for (int i = hc.getChildControllers().size() - 1; i >= 0; i--) {
					BayesianController bc = hc.getChildControllers().get(i);
					if (bc.getView().contains(start)) {
						target = bc;
						dx = (int) (e.getX() - target.getView().getX());
						dy = (int) (e.getY() - target.getView().getY());

						return;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);

			if (target != null) {
				target.getView().setLocation((int) (e.getX() - dx),
						(int) (e.getY() - dy));
				view.repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			if (target != null) {
				target.getView().setLocation(
						(int) (Utilities.cellSize * Math.round(target.getView()
								.getX() / Utilities.cellSize)),
						(int) (Utilities.cellSize * Math.round(target.getView()
								.getY() / Utilities.cellSize)));
			}

			target = null;
			start = null;
			dx = 0;
			dy = 0;
		}

	}

	class BayesianChangeListener implements ChangeListener {
		private BayesianController controller;

		public BayesianChangeListener(BayesianController c) {
			this.controller = c;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			JTextField field = controller.getControls().getField();

			double value = slider.getValue() / 100.0;
			field.setText("" + value);

			controller.getModel().setValue(value);
			controller.setCheckPartners(true);
			controller.update();

			updatePeers();
			
			// an hour of refactoring just to make this one statement
			view.repaint();
		}

	}
}