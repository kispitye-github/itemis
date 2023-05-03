package hu.kispitye.itemis.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.dto.UserDto;
import hu.kispitye.itemis.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static hu.kispitye.itemis.ItemisConstants.*;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;
	
	@Value("${"+PATH_LOGOUT+"}")
	private String logoutPath;

	@Value("${"+PATH_ROOT+"}")
	private String rootPath;

	@Value("${"+PATH_REGISTER+"}")
	private String registerPath;

    @GetMapping("${"+PATH_REGISTER+"}")
    public String showRegistrationForm(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:"+logoutPath;
        model.addAttribute(ATTRIBUTE_USER, new UserDto());
        return VIEW_REGISTER;
    }

    @PostMapping("${"+PATH_REGISTER+"}")
    public String registration(@ModelAttribute(ATTRIBUTE_USER) UserDto userData,
                               BindingResult result,
                               Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:"+rootPath;
        User existingUser = userService.findUser(userData.name);

        if (existingUser != null) result.rejectValue(FIELD_NAME, "username.exists", "");
        String pwd=userData.pwd!=null && userData.pwd.trim().length()>0 ? userData.pwd.trim() : null; 

        if (pwd==null || !pwd.equals(userData.pwd2)) result.rejectValue(FIELD_PWD, "pwd.mismatch", "");

        if (result.hasErrors()) {
            model.addAttribute(ATTRIBUTE_USER, userData);
            return VIEW_REGISTER;
        }

        userService.createUser(userData.name, userData.pwd);
        return "redirect:"+registerPath+"?"+PARAM_SUCCESS;
    }
}