package model;

import java.util.ArrayList;

/**
 * 
 * @author Jalal
 * @version 06/24/15
 * This class will model a variable's data in the Bayesian Theorem, holding identifiers and mutable values 
 */
public class BayesianModel {
	private String name, symbol, description;
	private double value;
	private ArrayList<BayesianModel> partners;
	
	private BayesianModel(Builder builder) {
		this.name		= builder.name;
		this.symbol		= builder.symbol;
		this.value		= builder.value;
		this.partners	= setPartners(builder.partners);
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

	public ArrayList<BayesianModel> getPartners() {
		return partners;
	}

	public String getDescription() {
		return description;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isPartnered(BayesianModel partner) {
		for(BayesianModel p:partners) {
			if(p.equals(partner))
				return true;
		}
		
		return false;
	}
	
	public void addPartner(BayesianModel partner) {
		this.partners.add(partner);
	}
	
	/**
	 * Sets the parameter as the partner of this object.
	 * Also attempts to set the partner's partner to this object.
	 * Also attempts to set the partner's value to 1.0 - this object's value
	 * It should be noted that Double.Nan == Double.NaN returns false! That cost me a good 15 minutes
	 * @param partner
	 * @return
	 */
	public ArrayList<BayesianModel> setPartners(ArrayList<BayesianModel> partners) {
		if(partners.isEmpty()){
			return partners;
		}
		
		for(BayesianModel partner : partners) {
			if(partner.getPartners().isEmpty() || !partner.isPartnered(this)) {
				partner.addPartner(this);;

				if(Double.isNaN(this.value)) {
					this.setValue(1.0 - partner.getValue());
				}
			}
		}
				
		return partners;
	}

	public boolean hasPartners() {
		return !this.partners.isEmpty();
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
		private ArrayList<BayesianModel> partners;
		
		public Builder(String name) {
			this.name = name;
			this.value = Double.NaN;
			this.symbol = "";
			this.description = "";
			this.partners = new ArrayList<BayesianModel>();
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
			this.partners.add(partner);
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
