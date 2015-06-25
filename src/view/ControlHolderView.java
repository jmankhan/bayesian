package view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import misc.Utilities;

public class ControlHolderView extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<ControlView> childViews;
	private JButton addNew;
	private JComboBox<String> combo;
	
	public ControlHolderView() {
		setup();
	}
	
	public ControlHolderView(ArrayList<ControlView> childViews) {
		this.childViews = childViews;
		for(ControlView c : childViews) {
			add(c);
		}
	}
	
	public void addChildren(ArrayList<ControlView> children) {
		this.childViews = children;
		for(ControlView c:children)
			add(c);
	}
	
	public void removeChildren() {
		for(ControlView c:childViews) {
			remove(c);
		}
		childViews.clear();
	}
	
	public void setup() {
		setBackground(Utilities.background);
		setPreferredSize(Utilities.prefSize);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		PersistentControls pControls = new PersistentControls();
		addNew = new JButton("+");
		combo = new JComboBox<String>();
		
		pControls.add(addNew, BorderLayout.NORTH);
		pControls.add(combo, BorderLayout.CENTER);
		
		add(pControls);
	}

	public JButton getButton() {
		return this.addNew;
	}

	public JComboBox<String> getComboBox() {
		return this.combo;
	}

	private class PersistentControls extends JPanel {
		
		private static final long serialVersionUID = -8991986919768291139L;

		public PersistentControls() {
			setup();
		}
		
		public void setup() {
			setBackground(Utilities.background);
			setPreferredSize(Utilities.prefSize);
			setLayout(new BorderLayout());
		}
	}
}