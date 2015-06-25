package controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import misc.Utilities;
import model.HypothesisModel;
import view.BayesianView;
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
public class HypothesisHolderController implements UpdateListener {

	private ArrayList<HypothesisController> childControllers;
	private HypothesisHolderView view;

	public HypothesisHolderController(HypothesisHolderView view,
			ArrayList<HypothesisModel> model) {
		Utilities.hypothesisListeners.add(this);
		this.view = view;
		setup(view, model);
	}

	public void setup(HypothesisHolderView view,
			ArrayList<HypothesisModel> models) {

		childControllers = new ArrayList<HypothesisController>();

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

			childControllers
					.add(new HypothesisController(hView, models.get(i)));
			view.add(hView);
		}

		// add mouselistener
		BayesianMouseAdapter adapter = new BayesianMouseAdapter();
		view.addMouseListener(adapter);
		view.addMouseMotionListener(adapter);
	}

	public void addNewHypothesis(HypothesisModel model) {

		HypothesisView last = childControllers.get(childControllers.size() - 1)
				.getView();
		int offX = last.x + last.width;
		int offY = last.y;
		int width = last.width;
		int height = last.height;

		HypothesisView view = new HypothesisView(offX, offY, width, height);

		this.view.add(view);
		childControllers.add(new HypothesisController(view, model));
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
}