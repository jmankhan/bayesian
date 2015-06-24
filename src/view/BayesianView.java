package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import misc.ScaleDirection;
import event.ChildUpdateListener;

public class BayesianView extends HypothesisView {
	private static final long serialVersionUID = 1L;
	private Color color;
	private ScaleDirection dir;
	private static ArrayList<ChildUpdateListener> listeners = new ArrayList<ChildUpdateListener>();
	private boolean isSelected;

	public BayesianView() {
		super();
	}
	
	public BayesianView(int x, int y, int w, int h) {
		super(x,y,w,h);
		isSelected = false;
	}
	
	public BayesianView(int x, int y, int w, int h, Color c) {
		super(x,y,w,h);
		this.color = c;
	}

	public BayesianView(int x, int y, int w, int h, ScaleDirection dir) {
		super(x,y,w,h);
		this.dir = dir;
		color = Color.black;
	}
	
	public BayesianView(int x, int y, int w, int h, Color c, ScaleDirection dir) {
		super(x,y,w,h);
		this.color = c;
		this.dir = dir;
		isSelected = false;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fill(this);
		updateListener();
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void addUpdateListener(ChildUpdateListener l) {
		listeners.add(l);
	}
	
	
	public void updateListener() {
		for(ChildUpdateListener l:listeners) {
			l.updateRequest();
		}
	}
}
