package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import hu.kispitye.itemis.security.WebSecurity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    // handler method to handle home page request
    @GetMapping("/")
    public String home(Model model) {
    	model.addAttribute("username", WebSecurity.getUserName());
        return "index";
    }
    
}