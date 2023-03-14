package hu.kispitye.itemis.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "items",
	uniqueConstraints={@UniqueConstraint(columnNames ={"user_id","name"})})
public class Item {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 42)
	private String name;
	
	@Column(nullable = false)
	private BigDecimal price;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	public User getUser() {
		return user;
	}

	public Item setUser(User user) {
		this.user = user;
		return this;
	}

	Item() {}

	public Item(User user, String name, BigDecimal price) {
		setUser(user);
		setName(name);
		setPrice(price);
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Item setName(String name) {
		this.name = name;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Item setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}
	
	@Override
	public String toString() {
		return "["+getName()+" = "+getPrice()+"]";
	}

	@Override
	public boolean equals(Object i) {
		if (!(i instanceof Item)) return false;
		if (i==this) return true;
		Item item=(Item)i;
		return getUser().equals(item.getUser())&&getName().equalsIgnoreCase(item.getName())&&getPrice().equals(item.getPrice());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}

