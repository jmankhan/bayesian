package model;

import java.util.HashMap;

import misc.ScaleDirection;

/**
 * 
 * @author Jalal
 * @version 6/24/15 This model will serve as a container for the BayesianModel
 *          by instantiating and easily accessing each individual model
 */
public class HypothesisModel {

	/**
	 * Keep track of how many hypothesis models have been created. Make sure to
	 * decrement when no longer using a hypothesis
	 */
	public static int hypotheses = 1;

	private BayesianModel[] data;

	private HashMap<String, BayesianModel> dataMap;

	/**
	 * Create default data
	 */
	public HypothesisModel() {
		dataMap = new HashMap<String, BayesianModel>();
		
		BayesianModel prh = new BayesianModel.Builder("prh").symbol("Pr(H)")
				.value(1.0).scaleDirection(ScaleDirection.LEFT_RIGHT).build();
		BayesianModel preh = new BayesianModel.Builder("preh")
				.symbol("Pr(E|H)").value(0.5)
				.scaleDirection(ScaleDirection.BOTTOM_TOP).build();
		BayesianModel prneh = new BayesianModel.Builder("prneh")
				.symbol("Pr(~E|H)").value(0.5)
				.scaleDirection(ScaleDirection.TOP_BOTTOM).build();

		BayesianModel[] data = { prh, preh, prneh };
		this.data = data;

		for (BayesianModel m : data) {
			dataMap.put(m.getName(), m);
		}
		
		hypotheses++;

	}

	public BayesianModel[] getData() {
		return this.data;
	}

	public HashMap<String, BayesianModel> getDataMap() {
		return dataMap;
	}
}
