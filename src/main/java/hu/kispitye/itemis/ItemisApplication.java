package hu.kispitye.itemis;

import java.util.Locale;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
@SpringBootApplication
public class ItemisApplication extends SpringBootServletInitializer implements WebMvcConfigurer {
	
	@Autowired
	UserService userService;

	@Bean
	LocaleResolver localeResolver() {
	    return new CookieLocaleResolver() {
	    	@Override
			public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
	    		if (locale!=null) {
	    			User user = userService.getCurrentUser();
	    			if (user!=null && !locale.equals(user.getLocale()))  {
	    				userService.updateUser(user.setLocale(locale));
	    				setDefaultLocale(locale);
	    			}
	    		}
	    		super.setLocale(request, response, locale);
			}
		};
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}
	
	@Bean
	Function<String, String> queryStringWithoutParam() {
	    return param ->   ServletUriComponentsBuilder.fromCurrentRequest().scheme(null).host(null).port(null).path(null).replaceQueryParam(param).toUriString();
	}
	
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(getClass());
   }	

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(localeChangeInterceptor());
   }
   
	public static void main(String[] args) {
		SpringApplication.run(ItemisApplication.class, args);
	}

}
