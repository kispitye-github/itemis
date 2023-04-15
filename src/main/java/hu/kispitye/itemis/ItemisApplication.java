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

import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
@SpringBootApplication
public class ItemisApplication extends SpringBootServletInitializer implements WebMvcConfigurer {
	
	public static final String LANG_EN="en";
	public static final String LANG_DE="de";
	public static final String LANG_HU="hu";
	public static final String PARAM_LANG="lang";
	
	public static final String VIEW_HEADER = "header";
	
	@Autowired(required=false)
	private UserService userService;

	@Bean
	LocaleResolver localeResolver() {
	    return new CookieLocaleResolver() {
	    	@Override
			public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
	    		if (locale!=null) {
	    			User user = userService.getCurrentUser();
	    			if (user!=null && !locale.equals(user.getLocale()))  {
	    				user.setLocale(locale);
	    				userService.updateUser(user, null);
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
	    lci.setParamName(PARAM_LANG);
	    return lci;
	}
	
	@Bean
	Function<String, String> queryStringWithLang() {
	    return lang ->   ServletUriComponentsBuilder.fromCurrentRequest().scheme(null).host(null).port(null).path(null).replaceQueryParam(PARAM_LANG, lang).toUriString();
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
