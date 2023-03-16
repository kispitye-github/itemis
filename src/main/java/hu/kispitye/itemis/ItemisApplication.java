package hu.kispitye.itemis;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.security.WebSecurity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
@SpringBootApplication
public class ItemisApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Bean
	LocaleResolver localeResolver() {
	    return new CookieLocaleResolver() {
	    	@Override
			public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
	    		if (locale!=null) {
	    			User user = WebSecurity.getUser();
	    			if (user!=null && !locale.equals(user.getLocale()))  {
	    				WebSecurity.saveUser(user.setLocale(locale));
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
	public Function<String, String> queryStringWithoutParam() {
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
