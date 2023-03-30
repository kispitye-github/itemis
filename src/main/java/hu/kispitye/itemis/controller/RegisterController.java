package hu.kispitye.itemis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hu.kispitye.itemis.model.User;
import hu.kispitye.itemis.model.transfer.UserDto;
import hu.kispitye.itemis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class RegisterController {

	public static final String PATH_REGISTER="path.register";
	public static final String ATTRIBUTE_USER="user";
	public static final String FIELD_NAME="name";
	public static final String FIELD_PWD="pwd";
	public static final String FIELD_PWD2="pwd2";
	public static final String PARAM_SUCCESS="success";
	
	private static final String VIEW_REGISTER="register";
	
	@Autowired
	private UserService userService;
	
	@Value("#{environment[homeController.PATH_ROOT]}")
	private String rootPath;

    @GetMapping("#{environment[registerController.PATH_REGISTER]}")
    public String showRegistrationForm(Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:"+rootPath;
        model.addAttribute(ATTRIBUTE_USER, new UserDto());
        return VIEW_REGISTER;
    }

    @PostMapping("#{environment[registerController.PATH_REGISTER]}")
    public String registration(@ModelAttribute(ATTRIBUTE_USER) UserDto userData,
                               BindingResult result,
                               Model model) {
    	if (userService.getCurrentUser()!=null) return "redirect:/";
        User existingUser = userService.findUser(userData.name);

        if (existingUser != null) result.rejectValue(FIELD_NAME, "username.exists", "");
        String pwd=userData.pwd!=null && userData.pwd.trim().length()>0 ? userData.pwd.trim() : null; 

        if (pwd==null || !pwd.equals(userData.pwd2)) result.rejectValue(FIELD_PWD, "pwd.mismatch", "");

        if (result.hasErrors()) {
            model.addAttribute(ATTRIBUTE_USER, userData);
            return VIEW_REGISTER;
        }

        userService.createUser(userData.name, userData.pwd);
        return "redirect:"+VIEW_REGISTER+"?"+PARAM_SUCCESS;
    }
}