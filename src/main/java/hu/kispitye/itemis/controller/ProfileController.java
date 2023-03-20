package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.transfer.*;
import hu.kispitye.itemis.service.UserService;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest.H2ConsoleRequestMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class ProfileController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UserService userService;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
    @GetMapping("/profile")
    public String showRegistrationForm(Model model) throws SQLException {
    	if (userService.getCurrentUser().isAdmin()) {
    		model.addAttribute("db", new DbDto().setUrl(url).setUser(username).setPwd(password));
    	}
        model.addAttribute("user", new UserDto(userService.getCurrentUser()));
        return "profile";
    }

    @PostMapping("/profile")
    public String registration(@ModelAttribute("user") UserDto userData,
                               BindingResult result,
                               Model model,
                               Locale locale) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        User existingUser = userService.findUser(userData.name);

        if (existingUser != null) result.rejectValue("name", "username.exists",
        	messageSource.getMessage("username.exists", null, null));
        if (!userData.pwd.equals(userData.pwd2)) result.rejectValue("pwd", "pwd.mismatch",
        	messageSource.getMessage("pwd.mismatch", null, null));

        if (result.hasErrors()) {
            model.addAttribute("user", userData);
            return "profile";
        }

        return "redirect:profile?success";
    }
}