package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.service.UserService;

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
	public static final String HOME_VIEW="login";

	@Autowired
	UserService userService;
	
	@Value("#{environment[homeController.PATH_ROOT]}")
	private String rootPath;
    
	@GetMapping("#{environment[loginController.PATH_LOGIN]}")
    public String login(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:"+rootPath;
        return HOME_VIEW;
    }
    
}