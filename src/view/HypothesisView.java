package view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import event.ChildUpdateListener;

/**
 * @author Jalal
 * @version 6/24/15
 * This class will display HypothesisModel data in a rectangular format. The value of the data will correspond with the size
 * of this view, while the value of the model's partner will determine this view's location. It will respond to commands from 
 * the controller only and will be responsible for drawing itself
 */
public class HypothesisView extends Rectangle {

	private static final long serialVersionUID = 1L;
	private ArrayList<BayesianView> childViews;
	
	public HypothesisView() {
		super();
	}
	
	public HypothesisView(int x, int y, int w, int h) {
		super(x,y,w,h);
		childViews = new ArrayList<BayesianView>();
	}

	
	/**
	 * Fills itself in with a color and draws the appropriate label on itself
	 * Size updates on repaint() call, which must come from controller command
	 * @param g
	 */
	public void draw(Graphics2D g) {
		for(BayesianView child : childViews) {
			child.draw(g);
		}
	}
	
	public void add(BayesianView child) {
		childViews.add(child);
	}
	
	public ArrayList<BayesianView> getChildren() {
		return this.childViews;
	}
}
