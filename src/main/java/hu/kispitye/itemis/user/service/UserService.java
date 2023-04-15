package hu.kispitye.itemis.user.service;

import hu.kispitye.itemis.user.User;

public interface UserService {
	User createUser(String name, String pwd);
	User updateUser(User user, String pwd);
	void deleteUser(User user);
	User findUser(String name);
	User getCurrentUser();
}
