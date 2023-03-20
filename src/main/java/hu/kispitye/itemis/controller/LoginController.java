package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.model.transfer.UserDto;
import hu.kispitye.itemis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class LoginController {

	public static final String PATH_LOGIN="path.login";
	public static final String PATH_LOGOUT="path.logout";
	
	@Autowired
	UserService userService;
	
	@GetMapping("#{environment[loginController.PATH_LOGIN]}")
    public String login(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        model.addAttribute("user", new UserDto());
        return "login";
    }
    
}