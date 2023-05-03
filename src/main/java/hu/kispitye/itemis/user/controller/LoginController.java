package hu.kispitye.itemis.user.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.user.service.UserService;

import static hu.kispitye.itemis.ItemisConstants.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class LoginController {

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