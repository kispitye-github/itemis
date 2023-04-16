package hu.kispitye.itemis.user;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import hu.kispitye.itemis.Security;
import hu.kispitye.itemis.dao.NamedEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@DynamicUpdate
public class User extends NamedEntity<User> implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	protected User() {} //JPA

	public User(String name, String pwd) {
		setUsername(name);
		setPassword(pwd);
	}

	@Column(nullable = true)
	protected Boolean admin;
	
	@Column(nullable = false, length = 64)
	protected String pwd;
	
	@Column(nullable = true, length = 16)
	@Convert(converter = User.LocaleConverter.class)
	protected Locale locale;
	
	@Override
	public boolean equals(Object u) {
		if (!(u instanceof User)) return false;
		return super.equals(u);
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
		return getName();
	}
	
	public User setUsername(String username) {
		setName(username);
		return this;
	}
	
	public boolean isAdmin() {
		return Boolean.TRUE.equals(admin);
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

	private static final List<GrantedAuthority> ADMIN_AUTHORITY = AuthorityUtils.createAuthorityList(Security.ADMIN_ROLE); 
	
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

	public static class LocaleConverter implements AttributeConverter<Locale, String> {
		
	    @Override
	    public String convertToDatabaseColumn(Locale locale) {
	        if (locale != null) {
	            return locale.toLanguageTag();
	        }
	        return null;
	    }
	
	    @Override
	    public Locale convertToEntityAttribute(String languageTag) {
	        if (languageTag != null && !languageTag.isEmpty()) {
	            return Locale.forLanguageTag(languageTag);
	        }
	        return null;
	    }
	}

}