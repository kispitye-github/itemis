package hu.kispitye.itemis.roman;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class RomanNumberTest {
	
	private static final String digits[][] = {
		{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},	
		{"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},	
		{"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},	
		{"", "M", "MM", "MMM"}	
	};
	
	private static final int MAXTESTLENGTH = 10;
	
	private void getValids(String prefix, int value, Set<RomanNumber> valids, int currentDigitIndex) {
		assertThat(currentDigitIndex<digits.length);
		int step = (int)Math.pow(10, currentDigitIndex);
		int digitValue = 0;
		for (String digit:digits[currentDigitIndex]) {
			final String number = prefix + digit;
			if (currentDigitIndex>0) getValids(number, value+digitValue, valids, currentDigitIndex-1);
			else if (number.length()>0) {
				RomanNumber romanNumber = RomanNumber.valueOf(number);
				assertThat(romanNumber.isValid()).isTrue();
				assertThat(romanNumber.getValue()).isEqualTo(value+digitValue);
				assertThat(valids.add(romanNumber)).isTrue();
			}
			digitValue += step;
		}
	}

	private void getValids(Set<RomanNumber> valids, int currentDigitIndex) {
		assertThat(currentDigitIndex<digits.length);
		getValids("", 0, valids, currentDigitIndex);
	}
	
	private Set<RomanNumber> getAllValids() {
		return getValids(digits.length);
	}
	
	private Set<RomanNumber> getValids(int maxDigits) {
		assertThat(maxDigits<=digits.length);
		int size = 1;
		for (int digit = 0; digit < maxDigits; digit++) {
			size *= digits[digit].length;
		}
		size--;
		HashSet<RomanNumber> valids = new HashSet<>(size*2);
		getValids(valids, maxDigits-1);
		assertThat(valids.size()).isEqualTo(size);
		HashSet<Integer> values =  new HashSet<>(size*2);
		for (RomanNumber number:valids) assertThat(values.add(number.getValue())).isTrue();
		assertThat(values.size()).isEqualTo(valids.size());
		for (int value=values.size(); value>0; value--) assertThat(values.remove(value)).isTrue();
		assertThat(values).isEmpty();
		System.out.println("Testing valid count: "+valids.size());
		return valids;
	}
	
	@Test
	void testValueOf() {
		HashSet<RomanNumber> valid = new HashSet<>();
		for (RomanNumeral numeral:RomanNumeral.values()) {
			valid.add(new RomanNumber(numeral));
		}
		assertThat(' ').isLessThan('Z');
		for (char ch=' '; ch<'Z'; ch++) try {
			RomanNumber number = RomanNumber.valueOf(String.valueOf(ch));
			assertThat(number.isValid()).isTrue();
			assertThat(valid.contains(number));
			valid.remove(number);
		} catch (NumberFormatException nfe) {}
		assertThat(valid).isEmpty();
	}

	@Test
	void testSingleNumerals() {
		for (RomanNumeral numeral:RomanNumeral.values()) {
			RomanNumber number = new RomanNumber(numeral);
			assertThat(number.isValid()).isTrue();
			assertThat(number.getValue()).isEqualTo(numeral.getValue());
		}
	}
	
	@Test
	void testAllValids() {
		Set<RomanNumber> valids = getAllValids();
		int maxLength =  valids.stream().mapToInt(number -> number.getStringValue().length()).max().getAsInt();
		System.out.println("Max valid length: "+maxLength);
		if (maxLength>MAXTESTLENGTH) {
			maxLength = MAXTESTLENGTH;
			System.out.println("Testing length limited to: "+maxLength);
		}
		for (int length=1; length<=maxLength; length++) testAllValids(valids, length);
	}
	
	private void testAllValids(Set<RomanNumber> valids, int length) {
		testAllValids(new ArrayDeque<RomanNumeral>(),  valids, length);
	}

	private void testAllValids(Deque<RomanNumeral> numerals, Set<RomanNumber> valids, int length) {
		assertThat(length).isLessThanOrEqualTo(MAXTESTLENGTH);
		for (RomanNumeral numeral:RomanNumeral.values()) {
			numerals.push(numeral);
			if (length==1) {
				RomanNumber number = new RomanNumber(numerals.toArray(RomanNumeral[]::new));
				assertThat(number.isValid()).isEqualTo(valids.contains(number));
				if (number.isValid()) valids.remove(number);
			} else testAllValids(numerals, valids, length-1);
			numerals.pop();
		}
	}

}
