package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.security.WebSecurity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    // handler method to handle home page request
    @GetMapping("/")
    public String home(Model model) {
    	User user = WebSecurity.getUser();
    	if (user!=null) {
    		model.addAttribute("username", user.getName());
    	}
        return "index";
    }
    
}