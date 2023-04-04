package hu.kispitye.itemis.model;

import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import hu.kispitye.itemis.security.WebSecurity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@DynamicUpdate
public class User extends UserBase implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	protected User() {} //JPA

	public User(String name, String pwd) {
		setUsername(name);
		setPassword(pwd);
	}

	private static final List<GrantedAuthority> ADMIN_AUTHORITY = AuthorityUtils.createAuthorityList(WebSecurity.ADMIN_ROLE); 
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return isAdmin()?ADMIN_AUTHORITY:AuthorityUtils.NO_AUTHORITIES;
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