package hu.kispitye.itemis.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import hu.kispitye.itemis.Security;
import hu.kispitye.itemis.dao.NamedEntityTest;
import hu.kispitye.itemis.user.User.LocaleConverter;

public class UserTest extends NamedEntityTest<User> {

	@Override
	protected User newEntity() {
		return new User();
	}

	@Override
	protected void testOtherFields(User user) {
		assertThat(user.getAdmin()).isNull();
		user.setAdmin(true);
		assertThat(user.getAdmin()).isTrue();
		assertThat(user.getPassword()).isNull();
		user.setPassword(user.toString());
		assertThat(user.getPassword()).isEqualTo(user.toString());
		assertThat(user.getLocale()).isNull();
		user.setLocale(Locale.getDefault());
		assertThat(user.getLocale()).isEqualTo(Locale.getDefault());
	}

	@Override
	protected void testOtherFields(User user1, User user2) {
		user1.setAdmin(Boolean.FALSE.equals(user1.getAdmin()));
		user2.setAdmin(!user1.getAdmin());
		testNameFields(user1, user2);
		user1.setPassword(user1.toString());
		user2.setPassword(user1.getPassword().toUpperCase());
		testNameFields(user1, user2);
		user1.setLocale(Locale.GERMAN);
		user2.setLocale(Locale.ENGLISH);
	}
	
	@Test
	void testUserDetails() {
		User user = new User(getClass().getName(), getClass().getTypeName());
		assertThat(user).isInstanceOf(UserDetails.class);
		assertThat(user.getUsername()).isEqualTo(getClass().getName());
		assertThat(user.getPassword()).isEqualTo(getClass().getTypeName());
		assertThat(user.isAccountNonExpired()).isTrue();
		assertThat(user.isAccountNonLocked()).isTrue();
		assertThat(user.isCredentialsNonExpired()).isTrue();
		assertThat(user.isAdmin()).isFalse();
		assertThat(user.getAuthorities()).isEmpty();
		user.setAdmin(true);
		assertThat(user.isAdmin()).isTrue();
		assertThat(user.getAuthorities()).isEqualTo(AuthorityUtils.createAuthorityList(Security.ADMIN_ROLE));
	}
	
	@Test
	void testLocaleConverter() {
		LocaleConverter converter = new LocaleConverter();
		Locale locale = null;
		assertThat(converter.convertToDatabaseColumn(locale)).isNull();
		locale = Locale.ENGLISH;
		String language = locale.toLanguageTag();
		assertThat(converter.convertToDatabaseColumn(locale)).isEqualTo(language);
		assertThat(converter.convertToEntityAttribute(language)).isEqualTo(locale);
		locale = Locale.GERMAN;
		language = locale.toLanguageTag();
		assertThat(converter.convertToDatabaseColumn(locale)).isEqualTo(language);
		assertThat(converter.convertToEntityAttribute(language)).isEqualTo(locale);
	}

}
