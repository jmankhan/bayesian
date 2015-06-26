package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BayesianView extends HypothesisView {
	private static final long serialVersionUID = 1L;
	private Color color;
	
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
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fill(this);
	}

}
