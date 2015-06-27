package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BayesianView extends HypothesisView {
	private static final long serialVersionUID = 1L;
	private Color color;
	private String text;
	
	public BayesianView() {
		super();
	}
	
	public BayesianView(int x, int y, int w, int h) {
		super(x,y,w,h);
	}
	
	public BayesianView(int x, int y, int w, int h, Color c) {
		super(x,y,w,h);
		this.color = c;
		this.text = "";
	}
	
	public BayesianView(int x, int y, int w, int h, Color c, String t) {
		super(x,y,w,h);
		this.color = c;
		this.text = t;
	}
	
	public void setText(String text) {
		this.text = text;
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

		g.setColor(Color.WHITE);
		int margin = 15;
		if(width < g.getFontMetrics().stringWidth(text) + margin)
			margin = 0;
		
		g.drawString(text, x+margin, y+height/2);
	}
	
}