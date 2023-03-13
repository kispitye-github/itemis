package hu.kispitye.itemis.model;

import java.util.Locale;

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
	
}