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
import event.ChildUpdateEvent;
import event.ChildUpdateEvent.Request;
import event.ChildUpdateListener;

/**
 * @author Jalal
 * @version 6/24/15 Takes in a controllerholderview (which has children
 *          containing controlviews + models). Will take requests from them to
 *          update, and will do so accordingly Will also add/remove
 *          controllers+views as needed
 */
public class ControlHolderController implements ChildUpdateListener {

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
		for (int i = 0; i < models.size(); i++)
			combo.addItem("Hypothesis " + (i+1));

		// add listener to combobox
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});

		// configure "add new hypothesis button"
		JButton addNew = view.getButton();
		addNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChildUpdateEvent event = new ChildUpdateEvent(
						Request.NEW_HYPOTHESIS);
				combo.addItem("Hypothesis " + HypothesisModel.hypotheses);
				updateRequest(event);
			}

		});

		view.addChildren(childViews);
		view.revalidate();
	}

	@Override
	public void updateRequest(ChildUpdateEvent e) {
		for (ChildUpdateListener l : Utilities.hypothesisListeners) {
			l.updateRequest(e);
		}
	}

}
