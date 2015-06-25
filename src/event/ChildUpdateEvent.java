package event;

public class ChildUpdateEvent {
	public enum Request {
		REPAINT, VALUE_CHANGE, NEW_HYPOTHESIS;
	}
	
	private Request req;
	private double value;
	
	public ChildUpdateEvent(Request r) {
		this.req = r;
	}
	
	public ChildUpdateEvent(Request r, double value) {
		this.req = r;
		this.value = value;
	}
	
	public Request getRequest() {
		return this.req;
	}
	
	public double getValue() {
		return this.value;
	}
}
