package hu.kispitye.itemis.service;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.UserWithUnitsAndItems;

public interface UserService {
	User createUser(String name, String pwd);
	User updateUser(User user, String pwd);
	void deleteUser(User user);
	User findUser(String name);
	User getCurrentUser();
	UserWithUnitsAndItems getCurrentUserWithUnitsAndItems();
}
