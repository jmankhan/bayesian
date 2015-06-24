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

/**
 * @author Jalal
 * @version 6/24/15 This controller will update the HypothesisHolderView but
 *          will not directly issue commands It will also instantiate the
 *          HypothesisControllers that belong to it and pass them their views
 *          and data It will receive events from them and pass them along
 *          
 *          TODO:fix second mouse click on same target
 */
public class HypothesisHolderController {

	private ArrayList<HypothesisController> childControllers;

	public HypothesisHolderController(HypothesisHolderView view,
			ArrayList<HypothesisModel> model) {
		setup(view, model);
	}

	public void setup(HypothesisHolderView view,
			ArrayList<HypothesisModel> models) {

		childControllers = new ArrayList<HypothesisController>();

		// prepare view array
		HypothesisView[] hViews = new HypothesisView[models.size()];

		// initialize constants to place each view
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
				if (hv.contains(start)) {
					for (BayesianView bv : hv.getChildren()) {
						if (bv.contains(start)) {
							target = bv;
							dx = (int) (e.getX() - target.getX());
							dy = (int) (e.getY() - target.getY());
							continue;
						}
					}
					// the concept of inheritance makes sense, but i can only
					// hope this works
					if (target == null)
						target = (BayesianView) hv;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);

			if (target != null) {
				target.setLocation((int) (e.getX() - dx), (int) (e.getY() - dy));

				target.updateListener();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			target.setLocation(
					(int) (Utilities.cellSize * Math.round(target.getX()
							/ Utilities.cellSize)),
					(int) (Utilities.cellSize * Math.round(target.getY()
							/ Utilities.cellSize)));
			
			target = null;
			dx = -1;
			dy = -1;
		}

	}
}