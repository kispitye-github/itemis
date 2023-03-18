package hu.kispitye.itemis.security;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Configuration
@EnableWebSecurity
public class WebSecurity implements AuthenticationSuccessHandler {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired
    private H2ConsoleProperties h2Console;
    
    @Bean
	static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	AntPathRequestMatcher h2ConsolePath = AntPathRequestMatcher.antMatcher(h2Console.getPath()+"/**");
        http
                .authorizeHttpRequests((authorize) ->
                        {
							try {
								authorize.requestMatchers("/register/**").permitAll()
								.requestMatchers("/webjars/**").permitAll()
								        .requestMatchers("/").permitAll()
								        .requestMatchers("/login/**").permitAll()
								        .requestMatchers("/error/**").permitAll()
								        .requestMatchers(h2ConsolePath).hasAuthority("ADMIN")
								        .anyRequest().authenticated()
								        .and().csrf().ignoringRequestMatchers(h2ConsolePath)
								        .and().headers().frameOptions().sameOrigin();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(this)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(AntPathRequestMatcher.antMatcher("/logout/**"))
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                ).sessionManagement(
                		session -> session
                			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
	                		.invalidSessionUrl("/error?invalid")
	                		.maximumSessions(1).expiredUrl("/error?expired")
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
		User user = userService.getCurrentUser();
		Locale locale = user.getLocale()==null?LocaleContextHolder.getLocale():user.getLocale();
		localeResolver.setLocale(request, response, locale);
		response.sendRedirect("/");
	}
}
