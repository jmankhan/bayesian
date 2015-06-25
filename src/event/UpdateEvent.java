package event;

import model.HypothesisModel;

public class UpdateEvent {
	public enum Request {
		REPAINT, VALUE_CHANGE, NEW_HYPOTHESIS;
	}
	
	private Request req;
	private double value;
	private HypothesisModel hModel;
	
	public UpdateEvent(Request r) {
		this.req = r;
	}
	
	public UpdateEvent(Request r, double value) {
		this.req = r;
		this.value = value;
	}
	
	public UpdateEvent(Request r, HypothesisModel h) {
		this.req = r;
		this.hModel = h;
	}
	
	public Request getRequest() {
		return this.req;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public HypothesisModel getHypothesisModel() {
		return this.hModel;
	}
}
