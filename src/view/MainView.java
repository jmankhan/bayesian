package view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainView() {
		setup();
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(1280, 720);
		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	}
}
