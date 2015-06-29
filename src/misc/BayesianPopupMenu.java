package misc;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BayesianPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -2730875668941789314L;
	private JMenuItem delete;
	
	public BayesianPopupMenu() {
		delete = new JMenuItem("Delete Hypothesis");
		add(delete);
	}
	
	public void addActionListener(ActionListener l) {
		delete.addActionListener(l);
	}
}