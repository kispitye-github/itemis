package hu.kispitye.itemis.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.repository.UserRepository;
import hu.kispitye.itemis.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(String name, String pwd, Locale locale) {
		User user = new User(name, passwordEncoder.encode(pwd)).setLocale(locale);
		user.setAdmin(user.isAdmin());
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		if (user.getAdmin()==null) user.setAdmin(user.isAdmin());
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);

	}

	@Override
	public User findUser(String name) {
		return userRepository.findByNameIgnoreCase(name);
	}

	@Override
	public User getCurrentUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) ?
	    		null:
	    		(User)authentication.getPrincipal();
    }
}
