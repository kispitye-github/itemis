package hu.kispitye.itemis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @PostMapping(value="/main", produces="text/plain")
    @ResponseBody
    public String main(@RequestParam("q") String q) {
        return "response="+q;
    }
    
}