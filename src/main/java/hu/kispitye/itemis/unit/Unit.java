package hu.kispitye.itemis.unit;

import org.hibernate.annotations.DynamicUpdate;

import hu.kispitye.itemis.roman.RomanNumeral;
import hu.kispitye.itemis.user.NamedEntityWithUser;
import hu.kispitye.itemis.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "units")
@DynamicUpdate
public class Unit extends NamedEntityWithUser<Unit> {
	
	@Column(nullable = false)
	RomanNumeral numeral;

	Unit() {}

	public Unit(User user, String name, RomanNumeral numeral) {
		super(user, name);
		setNumeral(numeral);
	}
	
	public RomanNumeral getNumeral() {
		return numeral;
	}

	public Unit setNumeral(RomanNumeral numeral) {
		this.numeral = numeral;
		return this;
	}
	
	@Override
	public String toString() {
		return "["+getName()+" = "+getNumeral()+"]";
	}

}