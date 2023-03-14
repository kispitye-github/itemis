package hu.kispitye.itemis.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.AuthorityUtils;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByNameIgnoreCase(name);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User( user.getName(),
                    user.getPwdHash(), AuthorityUtils.NO_AUTHORITIES);
        }else{
            throw new UsernameNotFoundException(name);
        }
    }

}