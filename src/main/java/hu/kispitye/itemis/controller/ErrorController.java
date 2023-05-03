package hu.kispitye.itemis.controller;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static hu.kispitye.itemis.ItemisConstants.*;


@Controller
public class ErrorController extends BasicErrorController {

	public ErrorController(ErrorAttributes errorAttributes) {
	  super(errorAttributes, new ErrorProperties());
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
		model.put(ATTRIBUTE_HEADER, response.isCommitted());
System.out.println("ERROR!" + model);		//TODO
		return super.errorHtml(request, response);
	}
}
