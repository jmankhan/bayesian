package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import misc.Utilities;

public class HypothesisControlsView extends JPanel {

	private static final long serialVersionUID = -6997048783742049234L;

	private JButton button;
	private JComboBox<String> combo;
	
	public HypothesisControlsView() {
		setup();
	}

	public void setup() {
		setBackground(Utilities.background);
		
		button = new JButton("+");
		add(button);
		
		combo  = new JComboBox<String>();
		combo.addItem("Hypothesis 1");
		add(combo);
	}
	
	public JButton getButton() {
		return button;
	}

	public JComboBox<String> getCombo() {
		return combo;
	}
}
