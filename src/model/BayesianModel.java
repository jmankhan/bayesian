package model;

/**
 * 
 * @author Jalal
 * @version 06/24/15
 * This class will model a variable's data in the Bayesian Theorem, holding identifiers and mutable values 
 */
public class BayesianModel {
	private String name, symbol, description;
	private double value;
	private BayesianModel partner;
	
	private BayesianModel(Builder builder) {
		this.name		= builder.name;
		this.symbol		= builder.symbol;
		this.value		= builder.value;
		this.partner	= setPartner(builder.partner);
		this.description= builder.description;
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

	public BayesianModel getPartner() {
		return partner;
	}

	public String getDescription() {
		return description;
	}

	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Sets the parameter as the partner of this object.
	 * Also attempts to set the partner's partner to this object.
	 * Also attempts to set the partner's value to 1.0 - this object's value
	 * It should be noted that Double.Nan == Double.NaN returns false! That cost me a good 15 minutes
	 * @param partner
	 * @return
	 */
	public BayesianModel setPartner(BayesianModel partner) {
		if(partner == null){
			return null;
		}
		
		this.partner = partner;
		if(partner.getPartner() == null || !partner.getPartner().equals(this)) {
			partner.setPartner(this);

			if(Double.isNaN(this.value)) {
				this.setValue(1.0 - partner.getValue());
			}
		}
		
		return partner;
	}

	public boolean hasPartner() {
		return this.partner != null;
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
		private BayesianModel partner;
		
		public Builder(String name) {
			this.name = name;
			this.value = Double.NaN;
			this.symbol = "";
			this.description = "";
			this.partner = null;
		}
		
		public Builder symbol(String symbol) {
			this.symbol = symbol;
			return this;
		}
		
		public Builder value(double value) {
			this.value = value;
			return this;
		}
		
		public Builder partner(BayesianModel partner) {
			this.partner = partner;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public BayesianModel build() {
			return new BayesianModel(this);
		}
	}
}
