package tests;

import static org.junit.Assert.*;
import model.BayesianModel;
import model.BayesianModel.Builder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks Builder pattern for BayesianModel class
 * Checks partner functionality for the BayesianModel class.
 * The rest of the methods are simple getters, so they will only be checked
 * if they have a potential to be null
 * @author Jalal
 *
 */
public class BayesianModelTest {

	BayesianModel test;
	@Before
	public void setUp() throws Exception {
		test = new BayesianModel.Builder("test").value(0.5).symbol("test symbol").build();
	}
	
	@After
	public void tearDown() throws Exception {
		test = null;
	}

	@Test
	public void testPartner() {
		BayesianModel partner = new BayesianModel.Builder("partner").value(0.1).partner(test).build();
		
		//check if has partner
		assertTrue(partner.hasPartner());
		
		//check properties of partner, making sure value is test.value = 1.0 - partner.value
		assertEquals(1.0-partner.getValue(), partner.getPartner().getValue(), 0.001);
		assertEquals("test", partner.getPartner().getName());
		
		//compare partner objects
		assertEquals(partner.getPartner(), test);
		
		//compare partner's partner to 'this' partner
		assertEquals(test.getPartner(), partner);
	}
	
	@Test
	public void testPartnerNoValue() {
		BayesianModel partner = new BayesianModel.Builder("partner").partner(test).build();
		
		//check if partner was matched
		assertTrue(partner.hasPartner());
		
		//check if value was untouched from initialized
		assertEquals(Double.NaN, partner.getValue(), 0.001);
		
		//check if partner's partner (test) value was untouched
		assertEquals(partner.getPartner().getValue(), test.getValue(), 0.001);
		assertEquals(0.5, test.getValue(), 0.001);
		
		//check if test was matched correctly with partner
		assertEquals(test.getPartner(), partner);
	}

}
