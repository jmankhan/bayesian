package controller;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import misc.Utilities;
import model.BayesianModel;
import view.ControlView;
import event.UpdateEvent;
import event.UpdateEvent.Request;
import event.UpdateListener;

/**
 * @author Jalal
 * @version 6/24/15
 * Receive input from user via the ControlView and change the data accordingly. Notify ControlHolderView and request an update
 *
 */
public class ControlController implements UpdateListener {

	private ControlView view;
	private BayesianModel model;
	
	public ControlController(ControlView view, BayesianModel model) {
		this.view = view;
		this.model = model;
		
		JSlider slider = view.getSlider();
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				model.setValue(source.getValue()/100.0);
				
				UpdateEvent event = new UpdateEvent(Request.VALUE_CHANGE, model.getValue());
				updateRequest(event);
			}
			
		});
	}

	public ControlView getView() {
		return view;
	}

	public BayesianModel getModel() {
		return model;
	}

	
	@Override
	public void updateRequest(UpdateEvent e) {
		for(UpdateListener l:Utilities.sliderListeners) {
			l.updateRequest(e);
		}
	}
}