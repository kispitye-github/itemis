package hu.kispitye.itemis.roman;

import java.util.*;

public class RomanNumber {
	private final RomanNumeral numerals[];
	
	private boolean valid;
	private int value;
	
	public RomanNumber(List<RomanNumeral> numerals) {
		this(numerals.toArray(RomanNumeral[]::new));
	}
	
	public RomanNumber(RomanNumeral... numerals) {
		this.numerals = numerals;
		validate();
	}

	public static RomanNumber valueOf(String number) throws NumberFormatException {
		RomanNumeral numerals[] = new RomanNumeral[number.length()];
		for (int index=numerals.length; --index>=0;) try {
			numerals[index] = RomanNumeral.valueOf(String.valueOf(number.charAt(index)));
		} catch (IllegalArgumentException eae)  {
			throw new NumberFormatException(eae.toString());
		}
		return new RomanNumber(numerals);
	}
	
	private void validate() {
		valid = numerals.length>0;
		RomanNumeral lastNumeral = null;
		RomanNumeral lastSubtractedNumeral = null;
		RomanNumeral lastAddedNumeral = null;
		int counter = 0;
		for (RomanNumeral numeral:numerals) {
			if (lastNumeral==numeral) {
				counter++;
				if (lastSubtractedNumeral!=null || counter>numeral.getMaxSuccession()) {
					valid=false;
					break;
				}
			} else {
				if (lastNumeral!=null) {
					if (lastNumeral.getValue()>numeral.getValue()) {
						if (lastSubtractedNumeral!=null) {
							if (lastSubtractedNumeral.getValue()<=numeral.getValue()) {
								valid=false;
								break;
							}
							lastSubtractedNumeral=null;
						}
						value+=lastNumeral.getValue()*counter;
						lastAddedNumeral = lastNumeral;
					} else if (counter>1 //substraction only from one digit
							|| lastSubtractedNumeral!=null //no chained subtractions
							|| !lastNumeral.canBeSubtracted() //not subtractable
							|| lastNumeral.getValue()<numeral.getValue()/10 //out of subtraction range
							|| lastAddedNumeral != null && lastAddedNumeral.getValue()<=numeral.getValue() && !lastAddedNumeral.canBeSubtracted()) { //substraction must be first in range, bad: VIV good: XIX
						valid=false;
						break;
					} else  {
						value-=lastNumeral.getValue();
						lastSubtractedNumeral = lastNumeral;
						lastAddedNumeral = null;
					}
				}
				lastNumeral=numeral;
				counter=1;
			}
		}
		if (valid) value+=lastNumeral.getValue()*counter;
		else value = -1;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public int getValue() throws NumberFormatException {
		if (!isValid()) throw new NumberFormatException("Invalid Roman Number: "+toString());
		return value;
	}
	
	public String getStringValue() {
		StringBuffer sb = new StringBuffer();
		for (RomanNumeral numeral:numerals) sb.append(numeral.name());
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(getStringValue());
		sb.append(" (");
		if (isValid()) sb.append(value);
		else sb.append("INVALID");
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o)  {
		if (!(o instanceof RomanNumber)) return false;
		if (o==this) return true;
		RomanNumber number = (RomanNumber)o;
		if (!isValid() || !number.isValid()) return false;
		return this.value==number.value;
	}

	@Override
	public int hashCode()  {
		return value;
	}

}
