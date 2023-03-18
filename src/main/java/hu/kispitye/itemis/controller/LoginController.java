package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.model.transfer.UserData;
import hu.kispitye.itemis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	
	@GetMapping("/login")
    public String login(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        model.addAttribute("user", new UserData());
        return "login";
    }
    
}