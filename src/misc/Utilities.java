package misc;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import event.UpdateListener;

public class Utilities {

	public static Color background = new Color(233,233,233);
	public static int cellSize = 20;
	public static Dimension prefSize = new Dimension(1280, 360);
	public static Color[] colors = {Color.black, Color.yellow, Color.BLUE, Color.GREEN, Color.red, Color.orange};
	
	public static ArrayList<UpdateListener> sliderListeners = new ArrayList<UpdateListener>();
	public static ArrayList<UpdateListener> hypothesisListeners = new ArrayList<UpdateListener>();
}
