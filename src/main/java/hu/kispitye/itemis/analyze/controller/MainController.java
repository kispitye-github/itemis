package hu.kispitye.itemis.analyze.controller;

import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.kispitye.itemis.analyze.service.AnalyzerService;

@Controller
public class MainController {

	public static final String PATH_MAIN="path.main";
	public static final String PARAM_Q="q";
	
	public static final String VIEW_MAIN = "analyze/main";

	private final static String ERROR_LIST="error.list";

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AnalyzerService analyzerService;
   
    @PostMapping(value="${"+PATH_MAIN+"}", produces="text/plain")
    @ResponseBody
    public String main(@RequestParam(PARAM_Q) String q) {
    	AnalyzerService.Result result = analyzerService.analyze(q);
    	Locale locale = LocaleContextHolder.getLocale();
        String s=result.getResult().stream().collect(Collectors.joining(" "));
        if (!result.getErrors().isEmpty()) 
        	s+=" ("+messageSource.getMessage(ERROR_LIST, null, locale)+" - "+
        			result.getErrors().stream().map( error -> "@"+error.sender()+": "+messageSource.getMessage(error.key(), error.params(), locale) ).collect(Collectors.joining("; "))+")";
        return s;
    }
    

}