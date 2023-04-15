package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

	public static final String PATH_ROOT="path.root";
	
	private static final String VIEW_HOME="index";

    @GetMapping("#{environment[homeController.PATH_ROOT]}")
    public String home(Model model) {
        return VIEW_HOME;
    }
    
}