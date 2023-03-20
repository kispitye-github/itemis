package hu.kispitye.itemis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ErrorController extends BasicErrorController {

	public static final String PATH_ERROR = "server.error.path";
	
	@Autowired
	public ErrorController(ErrorAttributes errorAttributes) {
	  super(errorAttributes, new ErrorProperties());
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
		System.out.println("ERROR"+model);
		return super.errorHtml(request, response);
	}
}
