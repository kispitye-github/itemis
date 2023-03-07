package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Locale;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RestController
public class WebController implements WebMvcConfigurer {
	@Autowired 
	private MessageSource messageSource;

	@GetMapping("/convert2")
	public String startConverter(@RequestHeader(name="Accept-Language", required=false) Locale locale) {
		return messageSource.getMessage("converter.start", null, locale);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/convert").setViewName("convert");
		registry.addViewController("/login").setViewName("login");
	}

}