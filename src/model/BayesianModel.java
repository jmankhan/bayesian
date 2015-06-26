package model;

import misc.ScaleDirection;

/**
 * 
 * @author Jalal
 * @version 06/24/15
 * This class will model a variable's data in the Bayesian Theorem, holding identifiers and mutable values 
 * I made the mistake of allowing the model to assign its own partners when that is really the controller's responsibility
 */
public class BayesianModel {
	
	private String name, symbol, description;
	private double value;
	private ScaleDirection scaleDirection;
	
	private BayesianModel(Builder builder) {
		this.name				= builder.name;
		this.symbol				= builder.symbol;
		this.value				= builder.value;
		this.description		= builder.description;
		this.scaleDirection		= builder.scaleDirection;
	}
	
	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public double getValue() {
		return value;
	}

	public ScaleDirection getScaleDirection() {
		return this.scaleDirection;
	}
	
	public String getDescription() {
		return description;
	}

	public void setValue(double value) {
		this.value = value;
	}


	/**
	 * @author Jalal
	 * @version 06/24/15
	 * Builder class for BayesianModel to easily add and remove fields from the class
	 * partner and value should preferably be set together
	 */
	public static class Builder {
		private String name, symbol, description;
		private double value;
		private ScaleDirection scaleDirection;
		
		public Builder(String name) {
			this.name = name;
			this.value = Double.NaN;
			this.symbol = "";
			this.description = "";
			scaleDirection = ScaleDirection.LEFT_RIGHT;
		}
		
		public Builder symbol(String symbol) {
			this.symbol = symbol;
			return this;
		}
		
		public Builder value(double value) {
			this.value = value;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder scaleDirection(ScaleDirection dir) {
			this.scaleDirection = dir;
			return this;
		}
		
		public BayesianModel build() {
			return new BayesianModel(this);
		}
	}
}
