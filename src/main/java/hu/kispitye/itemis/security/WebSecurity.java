package hu.kispitye.itemis.security;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.LocaleResolver;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurity implements AuthenticationSuccessHandler {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LocaleResolver localeResolver;

    private static UserService userService;

    public WebSecurity(UserService userService) {
    	WebSecurity.userService = userService;
    }

    @Bean
	public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static User getUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) ?
	    		null:
	    		userService.findUser(authentication.getName());
    }
    
    public static User saveUser(User user) {
    	return userService.updateUser(user);
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(this)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/")
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		User user = getUser();
		Locale locale = user.getLocale()==null?LocaleContextHolder.getLocale():user.getLocale();
		localeResolver.setLocale(request, response, locale);
		response.sendRedirect("/");
	}
}
