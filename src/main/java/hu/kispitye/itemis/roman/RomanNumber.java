package hu.kispitye.itemis.roman;

import java.util.*;

public class RomanNumber {
	private final List<RomanNumeral> numerals;
	
	private boolean valid = false;
	private int value = 0;
	
	public RomanNumber(List<RomanNumeral> numerals) {
		this.numerals = new ArrayList<>(numerals);
		validate();
	}
	
	private void validate() {
		valid = true;
		RomanNumeral lastNumeral = null;
		int counter = 0;
		for (RomanNumeral numeral:numerals) {
			if (lastNumeral==numeral) {
				counter++;
				if (counter>numeral.getMaxSuccession()) {
					valid=false;
					break;
				}
			} else {
				if (lastNumeral!=null) {
					if (lastNumeral.value()>numeral.value()) value+=lastNumeral.value()*counter;
					else if (counter>1 || !lastNumeral.canBeSubtracted() || lastNumeral.value()<numeral.value()/10) {
						valid=false;
						break;
					} else value-=lastNumeral.value();
				}
				lastNumeral=numeral;
				counter=1;
			}
		}
		if (counter==0) valid=false;
		if (!valid) return;
		value+=lastNumeral.value()*counter;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public int getValue() throws NumberFormatException {
		if (!isValid()) throw new NumberFormatException("Invalid Roman Number: "+toString());
		return value;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (RomanNumeral numeral:numerals) sb.append(numeral.name());
		sb.append(" (");
		if (isValid()) sb.append(value);
		else sb.append("INVALID");
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o)  {
		if (!(o instanceof RomanNumber)) return false;
		RomanNumber number = (RomanNumber)o;
		if (!isValid() || !number.isValid()) return this==number;
		return this.value==number.value;
	}

	@Override
	public int hashCode()  {
		return isValid()?value:super.hashCode();
	}

}
