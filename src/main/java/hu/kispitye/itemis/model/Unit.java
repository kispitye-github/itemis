package hu.kispitye.itemis.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "units",
	uniqueConstraints={@UniqueConstraint(columnNames ={"user_id","name"}),
			@UniqueConstraint(columnNames ={"user_id","numeral"})})
@SelectBeforeUpdate(false)
@DynamicUpdate
public class Unit {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 42)
	private String name;
	
	@Column(nullable = false)
	private RomanNumeral numeral;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserWithUnitsAndItems user;
	
	public UserWithUnitsAndItems getUser() {
		return user;
	}

	public Unit setUser(UserWithUnitsAndItems user) {
		this.user = user;
		return this;
	}

	Unit() {}

	public Unit(UserWithUnitsAndItems user, String name, RomanNumeral numeral) {
		setUser(user);
		setName(name);
		setNumeral(numeral);
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Unit setName(String name) {
		this.name = name;
		return this;
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

	@Override
	public boolean equals(Object u) {
		if (!(u instanceof Unit)) return false;
		if (u==this) return true;
		Unit unit=(Unit)u;
		return getUser().equals(unit.getUser())&&getName().equalsIgnoreCase(unit.getName())&&getNumeral()==unit.getNumeral();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}

