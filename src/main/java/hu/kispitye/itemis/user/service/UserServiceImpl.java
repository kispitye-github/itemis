package hu.kispitye.itemis.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.dao.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private List<String> adminNames = new ArrayList<>();
	
    @Autowired
    public void setAdminNames(@Value("#{'${admin.usernames:}'.split(',')}") List<String> adminNames) {
    	for (String adminName:adminNames) this.adminNames.add(adminName.trim().toLowerCase());
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Override
	@Transactional
	public User createUser(String name, String pwd) {
		return userRepository.persist(adjustUser(new User(name, null), pwd));
	}

	@Override
	@Transactional
	public User updateUser(User user, String pwd) {
		return userRepository.update(adjustUser(user, pwd));
	}

	@Override
	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUser(String name) {
		return userRepository.findByNameIgnoreCase(name);
	}

	@Override
	public User getCurrentUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication instanceof AnonymousAuthenticationToken || authentication==null || !authentication.isAuthenticated()) return null;
	    return adjustUser((User)authentication.getPrincipal(), null);
    }

    @Override
	@Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByNameIgnoreCase(name);
        if (user != null) return adjustUser(user, null);
        else throw new UsernameNotFoundException(name);
    }
    
    private User adjustUser(User user, String pwd)  {
		if (pwd!=null) user.setPassword(passwordEncoder.encode(pwd));
		if (user.getLocale()==null) user.setLocale(LocaleContextHolder.getLocale());
	    if (adminNames.contains(user.getUsername()) && !user.isAdmin()) user.setAdmin(true);
	    return user;
    }

}