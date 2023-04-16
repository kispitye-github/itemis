package hu.kispitye.itemis.user.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class LoginController {

	public static final String PATH_LOGIN="path.login";
	public static final String PATH_LOGOUT="path.logout";
	public static final String PARAM_LOGOUT="logout";
	public static final String PARAM_ERROR="error";

	public static final String FIELD_USERNAME="username";
	public static final String FIELD_PASSWORD="password";

	private static final String VIEW_LOGIN="user/login";

	@Autowired
	private UserService userService;
	
	@Value("${"+PATH_LOGOUT+"}")
	private String logoutPath;
    
	@GetMapping("${"+PATH_LOGIN+"}")
    public String login(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:"+logoutPath;
        return VIEW_LOGIN;
    }
    
}