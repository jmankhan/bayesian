package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;

import misc.Utilities;
import model.BayesianModel;
import model.HypothesisModel;
import view.ControlHolderView;
import view.ControlView;
import event.UpdateEvent;
import event.UpdateEvent.Request;
import event.UpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 Takes in a controllerholderview (which has children
 *          containing controlviews + models). Will take requests from them to
 *          update, and will do so accordingly Will also add/remove
 *          controllers+views as needed
 */
public class ControlHolderController implements UpdateListener {

	private ArrayList<ControlController> childControllers;

	public ControlHolderController(ControlHolderView view,
			ArrayList<HypothesisModel> models) {
		childControllers = new ArrayList<ControlController>();

		ArrayList<ControlView> childViews = new ArrayList<ControlView>();

		// populate child controllers by assigning them their appropriate data
		// and view
		for (HypothesisModel model : models) {
			for (BayesianModel m : model.getData()) {
				ControlView v = new ControlView(m.getSymbol(), m.getValue());
				childViews.add(v);
				childControllers.add(new ControlController(v, m));
			}
		}

		// populate combobox
		JComboBox<String> combo = view.getComboBox();
		for (int i = 0; i < HypothesisModel.hypotheses-1; i++) {
			combo.addItem("Hypothesis " + (i+1));
		}

		// add listener to combobox
		combo.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> box = (JComboBox<String>) e.getSource();
				int index = box.getSelectedIndex();
				
				//remove currently displayed controls
				view.removeChildren();
				//add relevent display controls
				ArrayList<ControlView> childViews = getVisibleViews(childControllers, index*3, index*3+3);
				view.addChildren(childViews);
				view.revalidate();
			}

		});

		// configure "add new hypothesis button"
		JButton addNew = view.getButton();
		addNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//create a new default model
				HypothesisModel model = new HypothesisModel();
				
				//create an event to notify hypthesisholdercontroller, passing along this model
				UpdateEvent event = new UpdateEvent(
						Request.NEW_HYPOTHESIS, model);
				
				//add new model option to combobox
				combo.addItem("Hypothesis " + (HypothesisModel.hypotheses-1));
				
				//update HypothesisHolderController
				updateRequest(event);
				
				
				for(BayesianModel bm : model.getData()) {
					ControlView c = new ControlView(bm.getSymbol(), bm.getValue());
					childControllers.add(new ControlController(c, bm));
				}
				
			}

		});

		view.addChildren(childViews);
		view.revalidate();
	}

	public ArrayList<ControlView> getVisibleViews(ArrayList<ControlController> controls, int start, int end) {
		ArrayList<ControlView> cViews = new ArrayList<ControlView>();
		for(int i=start; i<end; i++) {
			cViews.add(controls.get(i).getView());
		}
		return cViews;
	}
	
	@Override
	public void updateRequest(UpdateEvent e) {
		for (UpdateListener l : Utilities.hypothesisListeners) {
			l.updateRequest(e);
		}
	}

}
