package hu.kispitye.itemis.model;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserWithUnitsAndItems extends UserBase {

	@OneToMany(
		mappedBy = "user", 
	    cascade = CascadeType.ALL, 
	    orphanRemoval = true
	)
	private Set<Unit> units;
	
	@OneToMany(
			mappedBy = "user", 
		    cascade = CascadeType.ALL, 
		    orphanRemoval = true
		)
	private Set<Item> items;
		
	public Set<Unit> getUnits() {
		return units;
	}
	
	public UserWithUnitsAndItems addUnit(Unit unit) {
		units.add(unit);
		unit.setUser(this);
		return this;
	}

	public UserWithUnitsAndItems removeUnit(Unit unit) {
		units.remove(unit);
		unit.setUser(null);
		return this;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	
	public UserWithUnitsAndItems addItem(Item item) {
		items.add(item);
		item.setUser(this);
		return this;
	}
}