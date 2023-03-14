package hu.kispitye.itemis.service;

import hu.kispitye.itemis.model.User;

public interface UserService {
	User createUser(String name, String pwd);
	User updateUser(User user);
	void deleteUser(User user);
	User findUser(String name);
}
