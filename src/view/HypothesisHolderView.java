package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import misc.Utilities;
import event.ChildUpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 This view will display HypothesisViews in a Swing component
 *          (JPanel) and will only add or remove HypothesisViews
 */
public class HypothesisHolderView extends JPanel implements ChildUpdateListener {

	private static final long serialVersionUID = 1L;
	private ArrayList<HypothesisView> childViews;

	/**
	 * Instantiate empty view with no children
	 */
	public HypothesisHolderView() {
		childViews = new ArrayList<HypothesisView>();
		setup();
	}

	public void setup() {
		setBackground(Utilities.background);
		setPreferredSize(Utilities.prefSize);
	}

	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;

		drawGrid(g);

		// let each rectangle view draw itself
		for (HypothesisView view : childViews) {
			view.draw(g);
		}
	}

	/**
	 * Draw a grid on the panel using Lines
	 * 
	 * @param g
	 */
	public void drawGrid(Graphics2D g) {
		int width = getWidth();
		int height = getHeight();
		int cell = Utilities.cellSize;

		// draw vertical lines
		for (int i = cell; i < width; i += cell) {
			g.drawLine(i, 0, i, height);
		}

		// draw horizontal lines
		for (int i = cell; i < height; i += cell) {
			g.drawLine(0, i, width, i);
		}
	}

	/**
	 * Adds a new HypothesisView to this view and update (repaint) itself
	 */
	public void add(HypothesisView view) {
		childViews.add(view);
		for (BayesianView v : view.getChildren())
			v.addUpdateListener(this);
	}

	public ArrayList<HypothesisView> getChildren() {
		return childViews;
	}
	

	@Override
	public void updateRequest() {
		repaint();
	}
}
