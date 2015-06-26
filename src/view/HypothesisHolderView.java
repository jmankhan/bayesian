package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import misc.Utilities;

/**
 * @author Jalal
 * @version 6/24/15 This view will display HypothesisViews in a Swing component
 *          (JPanel) and will only add or remove HypothesisViews
 */
public class HypothesisHolderView extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<HypothesisView> childViews;
	private ArrayList<BayesianControlsView> controls;
	
	
	/**
	 * Instantiate empty view with no children
	 */
	public HypothesisHolderView() {
		childViews	= new ArrayList<HypothesisView>();
		controls	= new ArrayList<BayesianControlsView>();
		
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
	}

	public void addBCV(BayesianControlsView bcv) {
		controls.add(bcv);
		add(bcv);
	}
	
	public void removeBCV() {
		for(BayesianControlsView bcv : controls) {
			remove(bcv);
		}
		controls.clear();
	}
	public ArrayList<HypothesisView> getChildren() {
		return childViews;
	}
	
}
