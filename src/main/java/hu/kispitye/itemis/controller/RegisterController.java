package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.transfer.UserDto;
import hu.kispitye.itemis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class RegisterController {

	public static final String PATH_REGISTER="path.register";
	
	@Autowired
	private UserService userService;

    @GetMapping("#{environment[registerController.PATH_REGISTER]}")
    public String showRegistrationForm(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("#{environment[registerController.PATH_REGISTER]}")
    public String registration(@ModelAttribute("user") UserDto userData,
                               BindingResult result,
                               Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        User existingUser = userService.findUser(userData.name);

        if (existingUser != null) result.rejectValue("name", "username.exists", "");
        if (!userData.pwd.equals(userData.pwd2)) result.rejectValue("pwd", "pwd.mismatch", "");

        if (result.hasErrors()) {
            model.addAttribute("user", userData);
            return "register";
        }

        userService.createUser(userData.name, userData.pwd);
        return "redirect:register?success";
    }
}