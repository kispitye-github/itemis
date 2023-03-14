package hu.kispitye.itemis.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import hu.kispitye.itemis.model.transfer.UserData;
import hu.kispitye.itemis.security.WebSecurity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true, length = 42)
	private String name;
	
	@Column(nullable = false, length = 64)
	private String pwd;
	
	@Column(nullable = true, length = 16)
	@Convert(converter = UserData.LocaleConverter.class)
	private Locale locale;

	@OneToMany(
		mappedBy = "user", 
	    cascade = CascadeType.ALL, 
	    orphanRemoval = true
	)
	private Set<Unit> units = new HashSet<>();
	
	@OneToMany(
			mappedBy = "user", 
		    cascade = CascadeType.ALL, 
		    orphanRemoval = true
		)
	private Set<Item> items = new HashSet<>();
		
	User() {}
	
	public User(String name, String pwd) {
		setName(name);
		setPwd(pwd);
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getPwdHash() {
		return pwd;
	}

	public User setPwd(String pwd) {
		this.pwd = WebSecurity.passwordEncoder().encode(pwd);
		return this;
	}

	public Locale getLocale() {
		return locale;
	}

	public User setLocale(Locale locale) {
		this.locale = locale;
		return this;
	}
	
	public Set<Unit> getUnits() {
		return units;
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
		unit.setUser(this);
	}

	public void removeUnit(Unit unit) {
		units.remove(unit);
		unit.setUser(null);
	}
	
	public Set<Item> getItems() {
		return items;
	}
	
	public void addItem(Item item) {
		items.add(item);
		item.setUser(this);
	}
	
	@Override
	public String toString() {
		return "["+getName()+"]";
	}

	@Override
	public boolean equals(Object u) {
		if (!(u instanceof User)) return false;
		if (u==this) return true;
		User user=(User)u;
		return getName().equalsIgnoreCase(user.getName());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

}