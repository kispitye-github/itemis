package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import static hu.kispitye.itemis.ItemisConstants.*;

@Controller
public class HomeController {

    @GetMapping("${"+PATH_ROOT+"}")
    public String home(Model model) {
        return VIEW_HOME;
    }
    
}