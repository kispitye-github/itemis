package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.transfer.UserData;
import hu.kispitye.itemis.security.WebSecurity;
import hu.kispitye.itemis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class RegisterController {

	@Autowired
	private MessageSource messageSource;
	
	private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }
    
   
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
    	if (WebSecurity.getUserName()!=null) return "redirect:/";
        model.addAttribute("user", new UserData());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") UserData userData,
                               BindingResult result,
                               Model model) {
    	if (WebSecurity.getUserName()!=null) return "redirect:/";
        User existingUser = userService.findUser(userData.name);

        if(existingUser != null){
            result.rejectValue("name", "username.exists", messageSource.getMessage("username.exists", null, null));
        }
        if (!userData.pwd.equals(userData.pwd2)) {
            result.rejectValue("pwd", "pwd.mismatch",messageSource.getMessage("pwd.mismatch", null, null));
        } 

        if(result.hasErrors()){
            model.addAttribute("user", userData);
            return "register";
        }

        userService.createUser(userData.name, userData.pwd);
        return "redirect:register?success";
    }
}