package matrikelnr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VehicleCard implements Comparable<VehicleCard>{
	
	public enum Category{
		ECONOMY_MPG("Miles/Gallon"),
		CYLINDERS_CNT("Zylinder"),
		DISPLACEMENT_CCM("Hubraum[cc]"),
		POWER_HP("Leistung[hp]"),
		WEIGHT_LBS("Gewicht[lbs]"){
			@Override
			public boolean isInverted() {
				return true;
			}
		},
		ACCELERATION("Beschleunigung"){
			@Override
			public boolean isInverted() {
				return true;
			}
		},
		YEAR("Baujahr[19xx]");
		
		private final String categoryName;
		private Category(final String categoryName) throws IllegalArgumentException {
			if(categoryName == null) {
				throw new IllegalArgumentException("CategoryName cannot be null!");
			}
			if(categoryName.isEmpty()) {
				throw new IllegalArgumentException("CategoryName cannot be empty!");
			}
			this.categoryName = categoryName;
		}
		
		public boolean isInverted() {
			return false;
		}
		
		public int bonus(final Double value) {
			if(this.isInverted()) {
				return -value.intValue();
			}
			return value.intValue();
		}
		
		@Override
		public String toString() {
			return categoryName;
		}
	};
	
	private String name;
	private Map<Category, Double> categories;
	
	public VehicleCard(final String name, final Map<Category, Double> categories) throws IllegalArgumentException {
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null!");
		}
		if(name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
		if(categories == null) {
			throw new IllegalArgumentException("Categories cannot be null");
		}
		if(categories.isEmpty()) {
			throw new IllegalArgumentException("Categories cannot be empty");
		}
		if(categories.size() != Category.values().length) {
			throw new IllegalArgumentException("Categories must contain all category values!");
		}
		
		for(var x:categories.entrySet()) {
			if(x.getValue() == null) {
				throw new IllegalArgumentException("A value of categories cannot be null!");
			}
			if(x.getKey() == null) {
				throw new IllegalArgumentException("A value of categories cannot be null!");
			}
			if(x.getValue() < 0) {
				throw new IllegalArgumentException("A value of categories cannot be smaller than 0!");
			}
		}
		
		this.name = name;
		this.categories = new HashMap<Category, Double>(categories);
	}
	
	public String getName() {
		return name;
	}
	
	public Map<Category, Double> getCategories(){
		return new HashMap<Category, Double>(this.categories);
	}
	
	public static Map<Category, Double> newMap(double economy, double cylinders, double displacement,
			double power, double weight, double acceleration, double year){
		Map<Category, Double> res = new HashMap<Category, Double>();
		res.put(Category.ECONOMY_MPG, economy);
		res.put(Category.CYLINDERS_CNT, cylinders);
		res.put(Category.DISPLACEMENT_CCM, displacement);
		res.put(Category.POWER_HP, power);
		res.put(Category.WEIGHT_LBS, weight);
		res.put(Category.ACCELERATION, acceleration);
		res.put(Category.YEAR, year);
		return res;
	}

	@Override
	public int compareTo(final VehicleCard other) {
		return Integer.compare(this.totalBonus(), other.totalBonus());
	}
	
	public int totalBonus() {
		return categories.entrySet().stream().mapToInt(x -> x.getKey().bonus(x.getValue())).sum();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VehicleCard other = (VehicleCard) obj;
		return Objects.equals(name, other.name) && totalBonus() == other.totalBonus();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, totalBonus());
	}
	
	@Override
	public String toString() {
		boolean first = true;
		String output;
		output = "- " + getName() + "(" + totalBonus() + ") -> {";
		for(var x:categories.entrySet()) {
			if(!first) {
				output += ", ";
			}
			output += outputCategory(x.getKey())+"="+x.getValue();
			first = false;
		}
		output += "}";
		return output;
	}

	public String outputCategory(Category c) {
		return c.toString();
	}
	
}
