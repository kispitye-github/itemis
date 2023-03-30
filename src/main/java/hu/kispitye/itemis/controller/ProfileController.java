package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.transfer.*;
import hu.kispitye.itemis.service.UserService;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class ProfileController {

	public static final String PATH_PROFILE="path.profile";
	public static final String ATTRIBUTE_DB="db";
	public static final String ATTRIBUTE_CONSOLE="console";
	public static final String ATTRIBUTE_USER="user";
	public static final String FIELD_NAME="name";
	public static final String FIELD_PWD="pwd";
	public static final String FIELD_PWD2="pwd2";
	public static final String FIELD_ADMIN="admin";
	public static final String PARAM_SUCCESS="success";
	
	private static final String VIEW_PROFILE="profile";

	@Autowired
	private UserService userService;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
    @Autowired
    private H2ConsoleProperties h2Console;
	
    @GetMapping("#{environment[profileController.PATH_PROFILE]}")
    public String showProfileForm(Model model) throws SQLException {
    	User user = userService.getCurrentUser(); 
    	if (user.isAdmin()) {
    		model.addAttribute(ATTRIBUTE_DB, new DbDto(url, username, password));
    		model.addAttribute(ATTRIBUTE_CONSOLE, h2Console.getPath());
    	}
        model.addAttribute(ATTRIBUTE_USER, new UserDto(user));
        return VIEW_PROFILE;
    }

    @PostMapping("#{environment[profileController.PATH_PROFILE]}")
    public String changeProfile(@ModelAttribute(ATTRIBUTE_USER) UserDto userData,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUser(userData.name);
    	User user = userService.getCurrentUser(); 

        if (existingUser!=null && !existingUser.equals(user)) result.rejectValue(FIELD_NAME, "username.exists", "");
        String pwd=userData.pwd!=null && userData.pwd.trim().length()>0 ? userData.pwd.trim() : null; 
        if ( pwd!=null && !pwd.equals(userData.pwd2)) result.rejectValue(FIELD_PWD, "pwd.mismatch", "");

        if (result.hasErrors()) {
            model.addAttribute(ATTRIBUTE_USER, userData);
            userData.setAdmin(user.isAdmin());
            return VIEW_PROFILE;
        }
        
        user.setUsername(userData.name);
        userService.updateUser(user, pwd);

        return "redirect:"+VIEW_PROFILE+"?"+PARAM_SUCCESS;
    }
}