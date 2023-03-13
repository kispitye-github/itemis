package hu.kispitye.itemis.service.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.repository.UserRepository;
import hu.kispitye.itemis.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
	public User createUser(String name, String pwd) {
		return userRepository.save(new User(name, pwd));
	}

	@Override
	public User updateUser(User user, String name, String pwd, Locale locale) {
		return userRepository.save(user.setName(name).setPwd(pwd).setLocale(locale));
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);

	}

	@Override
	public User findUser(String name) {
		return userRepository.findByName(name);
	}

}
