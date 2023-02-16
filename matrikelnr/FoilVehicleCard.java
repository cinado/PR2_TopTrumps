package matrikelnr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FoilVehicleCard extends VehicleCard{
	private Set<Category> specials;

	public FoilVehicleCard(final String name, final Map<Category, Double> categories, final Set<Category> specials) throws IllegalArgumentException {
		super(name, categories);
		if(specials == null) {
			throw new IllegalArgumentException("Specials cannot be null!");
		}
		if(specials.isEmpty()) {
			throw new IllegalArgumentException("Specials cannot be empty!");
		}
		if(specials.size() > 3) {
			throw new IllegalArgumentException("Specials cannot contain more than 3 items!");
		}
		for(var s:specials) {
			if(s == null) {
				throw new IllegalArgumentException("A entry in specials cannot be null!");
			}
		}
		this.specials = new HashSet<Category>(specials);
	}
	
	public Set<Category> getSpecials(){
		return new HashSet<Category>(this.specials);
	}
	
	@Override
	public int totalBonus() {
		int sum = super.totalBonus();
		Map<Category, Double> temp = super.getCategories();
		for(var x:temp.entrySet()) {
			if(specials.contains(x.getKey())) {
				sum += Math.abs(x.getKey().bonus(x.getValue()));
			}
		}
		
		return sum;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	@Override
	public String outputCategory(Category c) {
		if(specials.contains(c)) {
			return "*" + c.toString() + "*";
		}
		return c.toString();
	}

}