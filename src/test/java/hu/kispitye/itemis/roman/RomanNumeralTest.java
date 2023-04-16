package hu.kispitye.itemis.roman;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RomanNumeralTest {

	private void test(RomanNumeral numeral, String name, int value) {
		assertThat(numeral.getValue()).isEqualTo(value);
		assertThat(RomanNumeral.valueOf(name)).isEqualTo(numeral);
		assertThat(numeral.name()).isEqualTo(name);
		for (RomanNumeral otherNumeral : RomanNumeral.values())
			if (otherNumeral.getValue()<value) assertThat(otherNumeral.compareTo(numeral)).isLessThan(0);
			else if (otherNumeral.getValue()>value) assertThat(otherNumeral.compareTo(numeral)).isGreaterThan(0);
			else assertThat(otherNumeral.compareTo(numeral)).isEqualTo(0);
	}
	
	@Test
	void testI() {
		RomanNumeral numeral = RomanNumeral.I; 
		test(numeral, "I", 1);
		assertThat(numeral.canBeSubtracted()).isTrue();
		assertThat(numeral.getMaxSuccession()).isEqualTo(3);
	}
	
	@Test
	void testV() {
		RomanNumeral numeral = RomanNumeral.V; 
		test(numeral, "V", 5);
		assertThat(numeral.canBeSubtracted()).isFalse();
		assertThat(numeral.getMaxSuccession()).isEqualTo(1);
	}
	
	@Test
	void testX() {
		RomanNumeral numeral = RomanNumeral.X; 
		test(numeral, "X", 10);
		assertThat(numeral.canBeSubtracted()).isTrue();
		assertThat(numeral.getMaxSuccession()).isEqualTo(3);
	}
	
	@Test
	void testL() {
		RomanNumeral numeral = RomanNumeral.L; 
		test(numeral, "L", 50);
		assertThat(numeral.canBeSubtracted()).isFalse();
		assertThat(numeral.getMaxSuccession()).isEqualTo(1);
	}
	
	@Test
	void testC() {
		RomanNumeral numeral = RomanNumeral.C; 
		test(numeral, "C", 100);
		assertThat(numeral.canBeSubtracted()).isTrue();
		assertThat(numeral.getMaxSuccession()).isEqualTo(3);
	}
	
	@Test
	void testD() {
		RomanNumeral numeral = RomanNumeral.D; 
		test(numeral, "D", 500);
		assertThat(numeral.canBeSubtracted()).isFalse();
		assertThat(numeral.getMaxSuccession()).isEqualTo(1);
	}
	
	@Test
	void testM() {
		RomanNumeral numeral = RomanNumeral.M; 
		test(numeral, "M", 1000);
		assertThat(numeral.canBeSubtracted()).isTrue();
		assertThat(numeral.getMaxSuccession()).isEqualTo(3);
	}
	
}
