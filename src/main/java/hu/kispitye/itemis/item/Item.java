package hu.kispitye.itemis.item;

import java.math.BigDecimal;

import org.hibernate.annotations.DynamicUpdate;

import hu.kispitye.itemis.user.NamedEntityWithUser;
import hu.kispitye.itemis.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
@DynamicUpdate
public class Item extends NamedEntityWithUser<Item> {
	
	@Column(nullable = false)
	BigDecimal price;
	
	Item() {}

	public Item(User user, String name, BigDecimal price) {
		super(user, name);
		setPrice(price);
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
}