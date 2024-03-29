package hu.kispitye.itemis;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static hu.kispitye.itemis.ItemisConstants.*;

import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Configuration
@EnableWebSecurity
public class Security implements AuthenticationSuccessHandler {
	
	private static final String ALLSUBPATHS = "/**";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required=false)
    private UserService userService;

    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired(required=false)
    private H2ConsoleProperties h2Console;
    
    @Bean
	static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Value("${"+PATH_LOGIN+"}")
	private String loginPath;
    
	@Value("${"+PATH_LOGOUT+"}")
	private String logoutPath;
    
	@Value("${"+PATH_REGISTER+"}")
	private String registerPath;
    
	@Value("${"+PATH_ERROR+"}")
	private String errorPath;
    
	@Value("${"+PATH_ROOT+"}")
	private String rootPath;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	AntPathRequestMatcher h2ConsolePath = h2Console==null?null:AntPathRequestMatcher.antMatcher(h2Console.getPath()+ALLSUBPATHS);
        http
                .authorizeHttpRequests(authorize ->
                        {
							authorize
					        	.requestMatchers(rootPath).permitAll()
								.requestMatchers("/webjars/**").permitAll()            //JQuery and BootStrap
						        .requestMatchers(loginPath+ALLSUBPATHS).permitAll()
								.requestMatchers(registerPath+ALLSUBPATHS).permitAll()
						        .requestMatchers(errorPath+ALLSUBPATHS).permitAll();
							if (h2ConsolePath!=null) try {
                                authorize.requestMatchers(h2ConsolePath).hasAuthority(ADMIN_ROLE);
                                http.csrf(csrf -> csrf.ignoringRequestMatchers(h2ConsolePath)).headers(headers -> headers.frameOptions( options -> options.sameOrigin()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							authorize.anyRequest().authenticated();
						}
                ).formLogin(
                        form -> form
                                .loginPage(loginPath)
                                .loginProcessingUrl(loginPath)
                                .successHandler(this)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(AntPathRequestMatcher.antMatcher(logoutPath+ALLSUBPATHS))
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                ).sessionManagement(
                		session -> session
                			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
	                		.invalidSessionUrl(errorPath+"?"+PARAM_INVALID)
	                		.maximumSessions(1).expiredUrl(errorPath+"?"+PARAM_EXPIRED)
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
		response.sendRedirect(rootPath);
	}
}
