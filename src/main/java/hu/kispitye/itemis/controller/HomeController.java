package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    // handler method to handle home page request
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
    
}