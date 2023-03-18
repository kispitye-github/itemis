package hu.kispitye.itemis.service;

import java.util.Locale;

import hu.kispitye.itemis.model.User;

public interface UserService {
	User createUser(String name, String pwd, Locale locale);
	User updateUser(User user);
	void deleteUser(User user);
	User findUser(String name);
	User getCurrentUser();
}
