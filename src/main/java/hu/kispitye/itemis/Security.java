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

import hu.kispitye.itemis.controller.ErrorController;
import hu.kispitye.itemis.controller.HomeController;
import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.controller.LoginController;
import hu.kispitye.itemis.user.controller.RegisterController;
import hu.kispitye.itemis.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Configuration
@EnableWebSecurity
public class Security implements AuthenticationSuccessHandler {
	
	public static final String ADMIN_ROLE = "ADMIN";
	
	private static final String ALLSUBPATHS = "/**";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired(required=false)
    private H2ConsoleProperties h2Console;
    
    @Bean
	static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Value("${"+LoginController.PATH_LOGIN+"}")
	private String loginPath;
    
	@Value("${"+LoginController.PATH_LOGOUT+"}")
	private String logoutPath;
    
	@Value("${"+RegisterController.PATH_REGISTER+"}")
	private String registerPath;
    
	@Value("${"+ErrorController.PATH_ERROR+"}")
	private String errorPath;
    
	@Value("${"+HomeController.PATH_ROOT+"}")
	private String rootPath;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	AntPathRequestMatcher h2ConsolePath = h2Console==null?null:AntPathRequestMatcher.antMatcher(h2Console.getPath()+ALLSUBPATHS);
        http
                .authorizeHttpRequests((authorize) ->
                        {
							authorize
					        	.requestMatchers(rootPath).permitAll()
								.requestMatchers("/webjars/**").permitAll()            //JQuery and BootStrap
						        .requestMatchers(loginPath+ALLSUBPATHS).permitAll()
								.requestMatchers(registerPath+ALLSUBPATHS).permitAll()
						        .requestMatchers(errorPath+ALLSUBPATHS).permitAll();
							if (h2ConsolePath!=null) try {
								authorize
						        	.requestMatchers(h2ConsolePath).hasAuthority(ADMIN_ROLE)
							        .and().csrf().ignoringRequestMatchers(h2ConsolePath)
							        .and().headers().frameOptions().sameOrigin();
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
	                		.invalidSessionUrl(errorPath+"?"+ErrorController.PARAM_INVALID)
	                		.maximumSessions(1).expiredUrl(errorPath+"?"+ErrorController.PARAM_EXPIRED)
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
