package hu.kispitye.itemis.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.dto.DbDto;
import hu.kispitye.itemis.user.dto.UserDto;
import hu.kispitye.itemis.user.service.UserService;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static hu.kispitye.itemis.ItemisConstants.*;


@Controller
public class ProfileController {

	@Autowired
	private UserService userService;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
    @Autowired(required=false)
    private H2ConsoleProperties h2Console;
	
    @GetMapping("${"+PATH_PROFILE+"}")
    public String showProfileForm(Model model) throws SQLException {
    	User user = userService.getCurrentUser(); 
    	if (user.isAdmin()) {
    		model.addAttribute(ATTRIBUTE_DB, new DbDto(url, username, password));
    		if (h2Console!=null) model.addAttribute(ATTRIBUTE_CONSOLE, h2Console.getPath());
    	}
        model.addAttribute(ATTRIBUTE_USER, new UserDto(user));
        return VIEW_PROFILE;
    }

    @Value("${"+PATH_PROFILE+"}")
    private String profilePath;
    
    @PostMapping("${"+PATH_PROFILE+"}")
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

        return "redirect:"+profilePath+"?"+PARAM_SUCCESS;
    }
}