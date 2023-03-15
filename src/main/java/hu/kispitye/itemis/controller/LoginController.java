package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.model.transfer.UserData;
import hu.kispitye.itemis.security.WebSecurity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    // handler method to handle home page request
    @GetMapping("/login")
    public String login(Model model) {
    	if (WebSecurity.getUser()!=null) return "redirect:/";
        model.addAttribute("user", new UserData());
        return "login";
    }
    
}