package event;

import java.util.EventListener;

/**
 * @author Jalal
 * @version 6/24/15
 * Child controller issues an update request that the parent receives and then updates its
 * views and data
 */
public interface ChildUpdateListener extends EventListener {
	public void updateRequest();
}