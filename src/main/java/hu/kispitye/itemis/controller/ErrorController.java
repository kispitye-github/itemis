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

@Controller
public class ErrorController extends BasicErrorController {

	public static final String PATH_ERROR = "server.error.path";
	
	public static final String PARAM_INVALID = "invalid";
	public static final String PARAM_EXPIRED = "expired";
	
	public static final String ATTRIBUTE_HEADER = "header";
	public static final String ATTRIBUTE_PATH = "path";
	public static final String ATTRIBUTE_TIMESTAMP = "timestamp";
	public static final String ATTRIBUTE_STATUS = "status";
	public static final String ATTRIBUTE_ERROR = "error";
	public static final String ATTRIBUTE_MESSAGE = "message";
	public static final String ATTRIBUTE_EXCEPTION = "exception";
	public static final String ATTRIBUTE_TRACE = "trace";

	public static final String VIEW_ERROR = "error";
	
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
