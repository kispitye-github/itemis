package hu.kispitye.itemis.controller;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.kispitye.itemis.analyze.RequestAnalyzer;
import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.service.*;

@Controller
public class MainController {

	private final static String MAIN_UNCLASSIFIED="main.unclassified";
	private final static String ERROR_LIST="error.list";
	private final static String QUESTION_UNKNOWN="question.unknown";
	private final static String DEFINITION_UNKNOWN="definition.unknown";
	private final static String ERROR_NUMERAL_DEFINITION_COUNT = "error.numeral.definition.count";
	private final static String UNIT_CREATED = "unit.created";
	private final static String UNIT_UPDATED_NAME = "unit.updated.name";
	private final static String UNIT_UPDATED_NUMERAL = "unit.updated.numeral";
	private final static String UNIT_KNOWN = "unit.known";
	private final static String UNIT_MERGED = "unit.merged";
	private final static String ERROR_UNIT_MISSING = "error.unit.missing";
	private final static String ERROR_INVALID_NUMERALS = "error.invalid.numerals";
	private final static String ERROR_UNKNOWN_UNIT = "error.unknown.unit";
	private final static String ERROR_PRICE_DEFINITION_ITEM = "error.price.definition.item";
	private final static String ERROR_PRICE_DEFINITION_COUNT = "error.price.definition.count";
	private final static String ITEM_CREATED = "item.created";
	private final static String ITEM_KNOWN = "item.known";
	private final static String ITEM_PRICE_CHANGED = "item.price.changed";
	private final static String ERROR_ITEM_MISSING = "error.item.missing";
	private final static String ERROR_ITEM_COUNT = "error.item.count";
	private final static String ERROR_UNKNOWN_ITEM = "error.unknown.item";
	
	@Autowired
	private MessageSource messageSource;
	
	private Locale locale;
	
	private String getMessage(String key, Object... args) {
		return messageSource.getMessage(key, args, locale);
	}
	
	private UserService userService;
	private UnitService unitService;
	private ItemService itemService;

    public MainController(UserService userService, UnitService unitService, ItemService itemService) {
        this.userService = userService;
        this.unitService = unitService;
        this.itemService = itemService;
    }
    
    private User user;
    
    @PostMapping(value="/main", produces="text/plain")
    @ResponseBody
    public String main(@RequestParam("q") String q, Locale locale) {
    	this.locale = locale;
    	List<String> result = new ArrayList<>();
    	RequestAnalyzer analyzer = new RequestAnalyzer(q, messageSource, locale);
		user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        if (analyzer.isQuestion()) answer(analyzer, result);
        else if (analyzer.isDefinition()) evaluate(analyzer, result);
        else analyzer.getErrors().put(MAIN_UNCLASSIFIED, "");
        String s=result.stream().reduce("", (String s1, String s2) -> s1+" "+s2);
        if (!analyzer.getErrors().isEmpty()) 
        	s+=" ("+getMessage(ERROR_LIST)+" - "+analyzer.getErrors().keySet().stream().reduce("", (String s1, String s2) -> (s1.length()==0?"":s1+"; ")+analyzer.getError(s2))+")";
        return s;
    }
    
    private void answer(RequestAnalyzer analyzer, List<String> result) {
    	if (analyzer.isNumberQuestion()) answerNumberQuestion(analyzer, result);
    	else if (analyzer.isPriceQuestion()) answerPriceQuestion(analyzer, result);
    	else analyzer.getErrors().put(QUESTION_UNKNOWN, "");
    }
	    
    private void answerPriceQuestion(RequestAnalyzer analyzer, List<String> result) {
    	if (!analyzer.getErrors().isEmpty()) return;
		List<String> tokens = analyzer.getRemainingTokens();
		if (tokens.size()>0) {
			String itemName = tokens.remove(tokens.size()-1);
			if (tokens.size()>0) {
				RomanNumber number = getRomanNumber(analyzer);
				if (analyzer.getErrors().isEmpty()) {
					if (number.isValid()) {
						Item item = itemService.findItemByName(user, itemName);
						if (item!=null)  {
							for (String token:tokens) result.add(token);
							result.add(itemName);
							result.add(getMessage(RequestAnalyzer.DEFINITION_PRICE_IS));
							result.add(item.getPrice().multiply(BigDecimal.valueOf(number.getValue())).toString());
							result.add(getMessage(RequestAnalyzer.QUESTION_CREDIT));
						}  else analyzer.getErrors().put(ERROR_UNKNOWN_ITEM, itemName);
					} else analyzer.getErrors().put(ERROR_INVALID_NUMERALS, number.toString());
				}
			} else analyzer.getErrors().put(ERROR_ITEM_COUNT, "");
		} else analyzer.getErrors().put(ERROR_ITEM_MISSING, "");
	}

	public RomanNumber getRomanNumber(RequestAnalyzer analyzer) {
		RomanNumber result = null;
		if (!analyzer.getErrors().isEmpty()) return result;
		Map<String, RomanNumeral> convert = new HashMap<>(analyzer.getRemainingTokens().size()); 
		List<RomanNumeral> numerals = new ArrayList<>(analyzer.getRemainingTokens().size());
		for (String token:analyzer.getRemainingTokens()) {
			if (!convert.containsKey(token)) {
				Unit unit = unitService.findUnitByName(user, token);
				if (unit==null) {
					analyzer.getErrors().put(ERROR_UNKNOWN_UNIT, token);
					convert.put(token, null);
				} else convert.put(token, unit.getNumeral());
			}
			RomanNumeral numeral = convert.get(token);
			if (numeral!=null) numerals.add(numeral);
		}
		if (analyzer.getErrors().isEmpty()) result=new RomanNumber(numerals);
		return result;
	}
	
	private void answerNumberQuestion(RequestAnalyzer analyzer, List<String> result) {
    	if (!analyzer.getErrors().isEmpty()) return;
		List<String> tokens = analyzer.getRemainingTokens();
		if (tokens.size()>0) {
			RomanNumber number = getRomanNumber(analyzer);
			if (analyzer.getErrors().isEmpty()) {
				if (number.isValid()) {
					for (String token:tokens) result.add(token);
					result.add(getMessage(RequestAnalyzer.DEFINITION_NUMERAL_IS));
					result.add(number.toString());
				} else analyzer.getErrors().put(ERROR_INVALID_NUMERALS, number.toString());
			}
		} else analyzer.getErrors().put(ERROR_UNIT_MISSING, "");
	}

	private void evaluate(RequestAnalyzer analyzer, List<String> result) {
		if (analyzer.isNumeralDefinition()) evaluateNumeral(analyzer, result);
		else if (analyzer.isPriceDefinition()) evaluatePrice(analyzer, result);
    	else analyzer.getErrors().put(DEFINITION_UNKNOWN, "");
    }

	private void evaluatePrice(RequestAnalyzer analyzer, List<String> result) {
    	if (!analyzer.getErrors().isEmpty()) return;
		List<String> tokens = analyzer.getRemainingTokens();
		if (tokens.size()==0) {
			analyzer.getErrors().put(ERROR_PRICE_DEFINITION_ITEM, "");
			return;
		}
		String itemName = tokens.remove(tokens.size()-1);
		if (tokens.size()==0) {
			analyzer.getErrors().put(ERROR_PRICE_DEFINITION_COUNT, "");
			return;
		}
		RomanNumber number = getRomanNumber(analyzer);
		if (analyzer.getErrors().isEmpty()) {
			if (number.isValid()) {
				BigDecimal price = BigDecimal.valueOf(analyzer.getPrice()).divide(BigDecimal.valueOf(number.getValue()));
				Item item=itemService.findItemByName(user, itemName);
				if (item==null) {
					item = itemService.createItem(user, itemName, price);
					result.add(getMessage(ITEM_CREATED, item));
				} else if (item.getPrice().compareTo(price)==0) {
					result.add(getMessage(ITEM_KNOWN, item));
				} else {
					BigDecimal oldPrice = item.getPrice();
					item.setPrice(price);
					itemService.updateItem(item);
					result.add(getMessage(ITEM_PRICE_CHANGED, oldPrice, item));
				}
			} else analyzer.getErrors().put(ERROR_INVALID_NUMERALS, number.toString());
		}
	}

	private void evaluateNumeral(RequestAnalyzer analyzer, List<String> result) {
    	if (!analyzer.getErrors().isEmpty()) return;
		List<String> tokens = analyzer.getRemainingTokens();
		if (tokens.size()==1) {
			String unitName = tokens.get(0);
			Unit unitByName = unitService.findUnitByName(user, unitName);
			Unit unitByNumeral = unitService.findUnitByRomanNumeral(user, analyzer.getRomanNumeral());
			if (unitByName==null) {
				if (unitByNumeral==null) {
					Unit unit = unitService.createUnit(user, unitName, analyzer.getRomanNumeral());
					result.add(getMessage(UNIT_CREATED, unit));
				} else {
					String oldName=unitByNumeral.getName();
					unitByNumeral.setName(unitName);
					unitService.updateUnit(unitByNumeral);
					result.add(getMessage(UNIT_UPDATED_NAME, oldName, unitByNumeral));
				}
			} else if (unitByNumeral==null) {
				RomanNumeral oldNumeral = unitByName.getNumeral();
				unitByName.setNumeral(analyzer.getRomanNumeral());
				unitService.updateUnit(unitByName);
				result.add(getMessage(UNIT_UPDATED_NUMERAL, oldNumeral, unitByName));
			} else if (unitByName.equals(unitByNumeral)) {
				result.add(getMessage(UNIT_KNOWN, unitByName));
			} else {
				String u1 = unitByName.toString();
				String u2 = unitByNumeral.toString();
				user.removeUnit(unitByNumeral);
				unitService.deleteUnit(unitByNumeral);
				unitByName.setNumeral(analyzer.getRomanNumeral());
				unitService.updateUnit(unitByName);
				result.add(getMessage(UNIT_MERGED, u1, u2, unitByName));
			}
 
		} else analyzer.getErrors().put(ERROR_NUMERAL_DEFINITION_COUNT, "");
	}

}