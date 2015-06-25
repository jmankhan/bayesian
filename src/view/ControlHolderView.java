package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import misc.Utilities;
import event.ChildUpdateEvent;
import event.ChildUpdateEvent.Request;
import event.ChildUpdateListener;

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
	
	public void setup() {
		setBackground(Utilities.background);
		setPreferredSize(Utilities.prefSize);
		setLayout(new GridLayout(3,3));

		addNew = new JButton("+");
		combo = new JComboBox<String>();
		
		add(addNew);
		add(combo);
	}

	public JButton getButton() {
		return this.addNew;
	}

	public JComboBox<String> getComboBox() {
		return this.combo;
	}
	
}