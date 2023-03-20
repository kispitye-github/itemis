package hu.kispitye.itemis.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.service.*;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

	private final static String QUESTION_UNKNOWN="question.unknown";
	public static final String QUESTION_NUMBER="question.number";
	public static final String QUESTION_NUMBER_IS="question.number.is";
	public static final String QUESTION_PRICE_IS="question.price.is";
	public static final String QUESTION_PRICE="question.price";
	public static final String QUESTION_CREDIT="question.credit";
	public static final String DEFINITION_PRICE_IS="definition.price.is";
	public static final String DEFINITION_NUMERAL_IS="definition.numeral.is";
	public static final String ERROR_NUMBER_FORMAT="error.number.format";
	public static final String ERROR_NUMBER_MISSING="error.number.missing";
	public static final String ERROR_PRICE_DEFINITION="error.price.definition";
	public static final String ERROR_NUMERAL_DEFINITION="error.numeral.definition";
	public static final String ERROR_NUMERAL_FORMAT="error.numeral.format";
	private final static String MAIN_UNCLASSIFIED="main.unclassified";
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
	

	@Override
	public Result analyze(String q) {
		return new Run(q);
	}
	
	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private MessageSource messageSource;
	
	private class Run implements Result {
		private Run(String q) {
			tokens = Collections.list(new StringTokenizer(q)).stream().map(token -> (String)token).collect(Collectors.toList());
			if (isQuestion()) answer();
			else if (isDefinition()) evaluate();
		    else addError(MAIN_UNCLASSIFIED);
		}
	
		private User user = userService.getCurrentUser();

		private String getMessage(String key, Object... args) {
			return messageSource.getMessage(key, args, user.getLocale());
		}

		
		private List<String> tokens;
		private List<Error> errors = new ArrayList<>();
		
		private void addError(String key, Object... params) {
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			String sender=trace[2].getMethodName();
			if (sender.startsWith("<")) sender="Run";
			errors.add(new Error(sender, key, params));
		}
		
		private List<String> result = new ArrayList<>();
		
		private boolean numberQuestion = false;
		private boolean priceQuestion = false;
	
		private boolean priceDefinition = false;
		private boolean numeralDefinition = false;
	
		
		private RomanNumeral romanNumeral = null;
		private int qty = 0;
		
		private boolean isQuestion() { 
	    	boolean result = false;
	    	if (tokens.size()>0) {
	    		result = tokens.get(tokens.size()-1).equals("?");
	    		if (result) tokens.remove(tokens.size()-1);
	    		if (tokens.size()==0) return result;
	    		String lastToken = tokens.remove(tokens.size()-1);
	    		if (lastToken.endsWith("?")) {
	    			result = true;
	    			lastToken=lastToken.substring(0, lastToken.length()-1).trim();
	    		}
	    		tokens.add(lastToken);
				priceQuestion = checkTokens(true, QUESTION_PRICE, QUESTION_CREDIT, QUESTION_PRICE_IS);
	    		if (priceQuestion) result=true;
	    		else {
	        		numberQuestion = checkTokens(true, QUESTION_NUMBER, QUESTION_NUMBER_IS);
	        		if (numberQuestion) result=true;
	    		}
	    	}
	    	return result;
	    }
	    
	    private boolean checkTokens(boolean fromStart, String... messageKeys) {
	    	boolean result = false;
			List<String> checks = Collections.list(new StringTokenizer(
			    Arrays.stream(messageKeys).map(messageKey -> getMessage(messageKey)).collect(Collectors.joining(" "))
				)).stream().map(token -> (String)token).collect(Collectors.toList());
			int offset = fromStart?0:tokens.size()-checks.size();
			if (tokens.size()>=checks.size()) {
				int index = checks.size()-1;
				while (index>=0 && tokens.get(offset+index).equalsIgnoreCase(checks.get(index))) index--;
				result = index<0;
			}
	    	if (result) tokens.subList(offset, offset+checks.size()).clear();
	    	return result;
	    }
		
	    private boolean isDefinition() {
	    	boolean result = false;
	    	if (tokens.size()>1) {
				priceDefinition = checkPriceDefinition();
	    		if (priceDefinition) result=true;
	    		else {
	        		numeralDefinition = checkNumeralDefinition();
	        		if (numeralDefinition) result=true;
	    		}
	    	}
	    	return result;
	    }
	    
	    private boolean checkNumeralDefinition() {
	    	String lastToken = tokens.remove(tokens.size()-1);
	    	boolean result = false;
	    	if (checkTokens(false, DEFINITION_NUMERAL_IS)) try {
				romanNumeral = RomanNumeral.valueOf(lastToken.toUpperCase());
				tokens.remove(tokens.size()-1);
				result = true;
			} catch (IllegalArgumentException ex) {
				addError(ERROR_NUMERAL_FORMAT, lastToken);
			}
			return result;
	    }
	    
	    private boolean checkPriceDefinition() {
	    	String lastToken = tokens.get(tokens.size()-1);
	    	boolean result = lastToken.equalsIgnoreCase(getMessage(QUESTION_CREDIT));
	    	if (!result) return result;
			tokens.remove(tokens.size()-1);
			if (tokens.size()>0) {
	    		lastToken = tokens.remove(tokens.size()-1);
	    		try {
	    			qty = Integer.valueOf(lastToken);
				} catch (NumberFormatException ex) {
					addError(ERROR_NUMBER_FORMAT, lastToken);
				}
	    		if (errors.isEmpty() && !checkTokens(false, DEFINITION_PRICE_IS)) addError(ERROR_PRICE_DEFINITION);
			} else addError(ERROR_NUMBER_MISSING);
	    	return result;
	    }
	    
	    private void answer() {
	    	if (numberQuestion) answerNumberQuestion();
	    	else if (priceQuestion) answerPriceQuestion();
	    	else addError(QUESTION_UNKNOWN);
	    }
	
	    private void answerPriceQuestion() {
	    	if (!getErrors().isEmpty()) return;
			if (tokens.size()>0) {
				String itemName = tokens.remove(tokens.size()-1);
				if (tokens.size()>0) {
					RomanNumber number = getRomanNumber();
					if (getErrors().isEmpty()) {
						if (number.isValid()) {
							Item item = itemService.findItemByName(user, itemName);
							if (item!=null)  {
								for (String token:tokens) result.add(token);
								result.add(itemName);
								result.add(getMessage(DEFINITION_PRICE_IS));
								result.add(item.getPrice().multiply(BigDecimal.valueOf(number.getValue())).toString());
								result.add(getMessage(QUESTION_CREDIT));
							}  else addError(ERROR_UNKNOWN_ITEM, itemName);
						} else addError(ERROR_INVALID_NUMERALS, number.toString());
					}
				} else addError(ERROR_ITEM_COUNT);
			} else addError(ERROR_ITEM_MISSING);
		}
	
		private RomanNumber getRomanNumber() {
			RomanNumber result = null;
			if (!getErrors().isEmpty()) return result;
			Map<String, RomanNumeral> convert = new HashMap<>(tokens.size()); 
			List<RomanNumeral> numerals = new ArrayList<>(tokens.size());
			for (String token:tokens) {
				if (!convert.containsKey(token)) {
					Unit unit = unitService.findUnitByName(user, token);
					if (unit==null) {
						addError(ERROR_UNKNOWN_UNIT, token);
						convert.put(token, null);
					} else convert.put(token, unit.getNumeral());
				}
				RomanNumeral numeral = convert.get(token);
				if (numeral!=null) numerals.add(numeral);
			}
			if (getErrors().isEmpty()) result=new RomanNumber(numerals);
			return result;
		}
		
		private void answerNumberQuestion() {
	    	if (!getErrors().isEmpty()) return;
			if (tokens.size()>0) {
				RomanNumber number = getRomanNumber();
				if (getErrors().isEmpty()) {
					if (number.isValid()) {
						for (String token:tokens) result.add(token);
						result.add(getMessage(DEFINITION_NUMERAL_IS));
						result.add(number.toString());
					} else addError(ERROR_INVALID_NUMERALS, number.toString());
				}
			} else addError(ERROR_UNIT_MISSING);
		}
	
		private void evaluate() {
			if (numeralDefinition) evaluateNumeral();
			else if (priceDefinition) evaluatePrice();
	    	else addError(DEFINITION_UNKNOWN);
	    }
	
		private void evaluatePrice() {
	    	if (!getErrors().isEmpty()) return;
			if (tokens.size()==0) {
				addError(ERROR_PRICE_DEFINITION_ITEM);
				return;
			}
			String itemName = tokens.remove(tokens.size()-1);
			if (tokens.size()==0) {
				addError(ERROR_PRICE_DEFINITION_COUNT);
				return;
			}
			RomanNumber number = getRomanNumber();
			if (getErrors().isEmpty()) {
				if (number.isValid()) {
					BigDecimal price = BigDecimal.valueOf(qty).divide(BigDecimal.valueOf(number.getValue()));
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
				} else addError(ERROR_INVALID_NUMERALS, number.toString());
			}
		}
	
		private void evaluateNumeral() {
	    	if (!getErrors().isEmpty()) return;
			if (tokens.size()==1) {
				String unitName = tokens.get(0);
				Unit unitByName = unitService.findUnitByName(user, unitName);
				Unit unitByNumeral = unitService.findUnitByRomanNumeral(user, romanNumeral);
				if (unitByName==null) {
					if (unitByNumeral==null) {
						Unit unit = unitService.createUnit(user, unitName, romanNumeral);
						result.add(getMessage(UNIT_CREATED, unit));
					} else {
						String oldName=unitByNumeral.getName();
						unitByNumeral.setName(unitName);
						unitService.updateUnit(unitByNumeral);
						result.add(getMessage(UNIT_UPDATED_NAME, oldName, unitByNumeral));
					}
				} else if (unitByNumeral==null) {
					RomanNumeral oldNumeral = unitByName.getNumeral();
					unitByName.setNumeral(romanNumeral);
					unitService.updateUnit(unitByName);
					result.add(getMessage(UNIT_UPDATED_NUMERAL, oldNumeral, unitByName));
				} else if (unitByName.equals(unitByNumeral)) {
					result.add(getMessage(UNIT_KNOWN, unitByName));
				} else {
					String u1 = unitByName.toString();
					String u2 = unitByNumeral.toString();
					user.removeUnit(unitByNumeral);
					unitService.deleteUnit(unitByNumeral);
					unitByName.setNumeral(romanNumeral);
					unitService.updateUnit(unitByName);
					result.add(getMessage(UNIT_MERGED, u1, u2, unitByName));
				}
	 
			} else addError(ERROR_NUMERAL_DEFINITION_COUNT);
		}
	
	    
	    
		@Override
		public List<String> getResult() {
			return result;
		}
	
		@Override
		public List<Error> getErrors() {
			return errors;
		}
	}
	    
}

	
