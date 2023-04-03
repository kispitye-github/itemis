package hu.kispitye.itemis.service;

import hu.kispitye.itemis.model.*;

public interface UnitService {
	Unit createUnit(UserWithUnitsAndItems user, String name, RomanNumeral numeral);
	Unit updateUnit(Unit unit);
	void deleteUnit(Unit unit);
	Unit findUnitByName(UserWithUnitsAndItems user, String name);
	Unit findUnitByRomanNumeral(UserWithUnitsAndItems user, RomanNumeral numeral);
}
