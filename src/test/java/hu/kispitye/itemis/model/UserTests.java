package hu.kispitye.itemis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hu.kispitye.itemis.security.WebSecurity;

class UserTests {

	@Test
	void testPwd() {
		User user = new User("name", "pwd");
		assertEquals("name", user.getName());
		String hash1 = user.getPwdHash();
		assertTrue(WebSecurity.passwordEncoder().matches("pwd", hash1));
		assertNotEquals("pwd", hash1);
	}

}
