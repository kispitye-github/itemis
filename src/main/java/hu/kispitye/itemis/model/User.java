package hu.kispitye.itemis.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import hu.kispitye.itemis.model.transfer.UserData;
import jakarta.persistence.*;

@Component
@Entity
@Table(name = "users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private static List<String> adminNames = new ArrayList<>();
	
    @Autowired
    public void setAdminNames(@Value("#{'${admin.usernames}'.split(',')}") List<String> adminNames) {
    	for (String adminName:adminNames) User.adminNames.add(adminName.trim().toLowerCase());
    }

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = true)
	private Boolean admin;
	
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
	private Set<Unit> units;
	
	@OneToMany(
			mappedBy = "user", 
		    cascade = CascadeType.ALL, 
		    orphanRemoval = true
		)
	private Set<Item> items;
		
	@SuppressWarnings("unused")
	private User() {}
	
	public User(String name, String pwd) {
		setUsername(name);
		setPassword(pwd);
	}
	
	public Long getId() {
		return id;
	}

	public boolean isAdmin() {
		return adminNames.contains(getUsername().toLowerCase()) || Boolean.TRUE.equals(admin);
	}

	public Boolean getAdmin() {
		return admin;
	}

	public User setAdmin(Boolean admin) {
		this.admin = admin;
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
	
	public User addUnit(Unit unit) {
		units.add(unit);
		unit.setUser(this);
		return this;
	}

	public User removeUnit(Unit unit) {
		units.remove(unit);
		unit.setUser(null);
		return this;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	
	public User addItem(Item item) {
		items.add(item);
		item.setUser(this);
		return this;
	}
	
	@Override
	public String toString() {
		return "["+getUsername()+"]";
	}

	@Override
	public boolean equals(Object u) {
		if (!(u instanceof User)) return false;
		if (u==this) return true;
		User user=(User)u;
		return getUsername().equalsIgnoreCase(user.getUsername());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return isAdmin()?AuthorityUtils.createAuthorityList("ADMIN"):AuthorityUtils.NO_AUTHORITIES;
	}

	@Override
	public String getPassword() {
		return pwd;
	}

	public User setPassword(String password) {
		pwd=password;
		return this;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public User setUsername(String username) {
		name=username.trim();
		return this;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}