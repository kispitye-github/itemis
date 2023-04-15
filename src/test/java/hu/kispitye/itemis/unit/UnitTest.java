package hu.kispitye.itemis.unit;

import static org.assertj.core.api.Assertions.assertThat;
import hu.kispitye.itemis.roman.RomanNumeral;
import hu.kispitye.itemis.user.NamedEntityWithUserTest;

public class UnitTest extends NamedEntityWithUserTest<Unit> {
	
	private static final RomanNumeral NUMERAL = RomanNumeral.I;
	private static final RomanNumeral NUMERAL2 = RomanNumeral.C;

	@Override
	protected Unit newEntity() {
		return new Unit();
	}

	@Override
	protected void testOtherFields(Unit unit) {
		assertThat(unit.getNumeral()).isNull();
		unit.setNumeral(NUMERAL);
		assertThat(unit.getNumeral()).isEqualTo(NUMERAL);
	}

	@Override
	protected void testOtherFields(Unit unit1, Unit unit2) {
		unit1.setNumeral(NUMERAL);
		unit2.setNumeral(NUMERAL2);
		assertThat(unit1.getNumeral()).isNotEqualTo(unit2.getNumeral());
	}
	
}
