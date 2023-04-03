package hu.kispitye.itemis.model;

import java.util.Locale;

import jakarta.persistence.*;

@MappedSuperclass
class UserBase {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = true)
	protected Boolean admin;
	
	@Column(nullable = false, unique = true, length = 42)
	protected String name;
	
	@Column(nullable = false, length = 64)
	protected String pwd;
	
	@Column(nullable = true, length = 16)
	@Convert(converter = User.LocaleConverter.class)
	protected Locale locale;
	
	@Override
	public String toString() {
		return "["+getUsername()+"]";
	}
	
	@Override
	public boolean equals(Object u) {
		if (!(u instanceof UserBase)) return false;
		if (u==this) return true;
		UserBase user=(UserBase)u;
		return getUsername().equalsIgnoreCase(user.getUsername());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public String getPassword() {
		return pwd;
	}
	
	public UserBase setPassword(String password) {
		pwd=password;
		return this;
	}
	
	public String getUsername() {
		return name;
	}
	
	public UserBase setUsername(String username) {
		name=username.trim();
		return this;
	}
	
	public Long getId() {
		return id;
	}
	
	public boolean isAdmin() {
		return Boolean.TRUE.equals(admin);
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	
	public UserBase setAdmin(Boolean admin) {
		this.admin = admin;
		return this;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public UserBase setLocale(Locale locale) {
		this.locale = locale;
		return this;
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