package hu.kispitye.itemis.analyze;

import java.util.*;
import java.util.stream.*;

import org.springframework.context.MessageSource;

import hu.kispitye.itemis.model.*;

public class RequestAnalyzer {

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
	
	private MessageSource messageSource;
	
	private String getMessage(String key, Object... args) {
		return messageSource.getMessage(key, args, null);
	}
	
	private List<String> tokens;
	private Map<String, String> errors = new LinkedHashMap<>();
	
	private boolean question = false;
	private boolean numberQuestion = false;
	private boolean priceQuestion = false;

	public boolean isPriceQuestion()  {
		return priceQuestion;
	}
	
	public boolean isNumberQuestion()  {
		return numberQuestion;
	}
	
	private boolean definition = false;
	private boolean priceDefinition = false;
	private boolean numeralDefinition = false;

	public boolean isPriceDefinition()  {
		return priceDefinition;
	}
	
	public boolean isNumeralDefinition()  {
		return numeralDefinition;
	}
	
	private RomanNumeral romanNumeral = null;
	private int price = 0;
	
	public RomanNumeral getRomanNumeral() {
		return romanNumeral;
	}
	
	public int getPrice() {
		return price;
	}
	
	public List<String> getRemainingTokens() {
		return tokens;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
	
	public String getError(String key) {
		String result = getMessage(key);
		if (result.contains("{0}")) result=getMessage(key, errors.get(key));
		else {
			String error = errors.getOrDefault(key, "");
			if (error.length()>0) result+=": "+error;
		}
		return result;
	}
	
	public RequestAnalyzer(String q, MessageSource messageSource) {
		this.messageSource = messageSource;
    	tokens = Collections.list(new StringTokenizer(q)).stream().map(token -> (String)token).collect(Collectors.toList());
    	question = getIsQuestion();
    	if (!question) definition = getIsDefinition();
	}

	public boolean isQuestion() {	
		return question;
	}
	
	private boolean getIsQuestion() { 
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
		    Arrays.stream(messageKeys).map(messageKey -> getMessage(messageKey)).reduce("", (String s1, String s2) -> s1+" "+s2)
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
	
    public boolean isDefinition() {
    	return definition;
    }
    
    private boolean getIsDefinition() {
    	boolean result = false;
    	if (tokens.size()>0) {
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
    	String lastToken = tokens.get(tokens.size()-1);
    	boolean result = false;
		try {
			romanNumeral = RomanNumeral.valueOf(lastToken.toUpperCase());
			result = true;
		} catch (IllegalArgumentException ex) {
			errors.put(ERROR_NUMERAL_FORMAT, lastToken);
		}
		if (errors.isEmpty())  {
			tokens.remove(tokens.size()-1);
    		if (!checkTokens(false, DEFINITION_NUMERAL_IS)) errors.put(ERROR_NUMERAL_DEFINITION, "");
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
    			price = Integer.valueOf(lastToken);
			} catch (NumberFormatException ex) {
				errors.put(ERROR_NUMBER_FORMAT, lastToken);
			}
    		if (errors.isEmpty() && !checkTokens(false, DEFINITION_PRICE_IS)) errors.put(ERROR_PRICE_DEFINITION, "");
		} else errors.put(ERROR_NUMBER_MISSING, "");
    	return result;
    }
}
