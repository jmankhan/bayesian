package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import misc.ScaleDirection;
import event.UpdateEvent;
import event.UpdateEvent.Request;
import event.UpdateListener;

public class BayesianView extends HypothesisView {
	private static final long serialVersionUID = 1L;
	private Color color;
	private ScaleDirection dir;
	private static ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();

	public BayesianView() {
		super();
	}
	
	public BayesianView(int x, int y, int w, int h) {
		super(x,y,w,h);
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
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public ScaleDirection getScaleDirection() {
		return this.dir;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fill(this);
		updateListener(new UpdateEvent(Request.REPAINT));
	}
	
	public void addUpdateListener(UpdateListener l) {
		listeners.add(l);
	}
	
	
	public void updateListener(UpdateEvent e) {
		for(UpdateListener l:listeners) {
			l.updateRequest(e);
		}
	}
}
