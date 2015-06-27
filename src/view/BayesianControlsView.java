package view;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * @author Jalal
 * @version 6/24/15
 * This class will contain a slider, textfield, and label to allow the user to manipulate the HypothesisView
 * This will send all user input to the ControlController
 */
public class BayesianControlsView extends JPanel {

	private static final long serialVersionUID = 7298497374965533616L;
	
	private JLabel label;
	private JTextField field;
	private JSlider slider;
	
	public BayesianControlsView(String text, double value) {

		DecimalFormat df = new DecimalFormat("#.00");
		
		label = new JLabel(text);
		field = new JTextField(2);
		field.setText(df.format(value));
		field.setEditable(false);
		slider = new JSlider(JSlider.HORIZONTAL);
		slider.setValue((int) (value*100)); 
		

		add(label);
		add(field);
		add(slider);
	}

	public JLabel getLabel() {
		return label;
	}

	public JTextField getField() {
		return field;
	}

	public JSlider getSlider() {
		return slider;
	}
}
