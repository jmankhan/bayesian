package event;

import model.BayesianModel;
import model.HypothesisModel;

public class UpdateEvent {
	public enum Request {
		REPAINT, VALUE_CHANGE, NEW_HYPOTHESIS;
	}
	
	private Request req;
	private BayesianModel bModel;
	private HypothesisModel hModel;
	
	public UpdateEvent(Request r) {
		this.req = r;
	}
	
	public UpdateEvent(Request r, BayesianModel b) {
		this.req = r;
		this.bModel = b;
	}
	
	public UpdateEvent(Request r, HypothesisModel h) {
		this.req = r;
		this.hModel = h;
	}
	
	public Request getRequest() {
		return this.req;
	}
	
	public BayesianModel getBayesianModel() {
		return this.bModel;
	}
	
	public HypothesisModel getHypothesisModel() {
		return this.hModel;
	}
}
