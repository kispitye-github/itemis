package hu.kispitye.itemis.service;

import hu.kispitye.itemis.model.*;

public interface UnitService {
	Unit createUnit(User user, String name, RomanNumeral numeral);
	Unit updateUnit(Unit unit);
	void deleteUnit(Unit unit);
	Unit findUnitByName(User user, String name);
	Unit findUnitByRomanNumeral(User user, RomanNumeral numeral);
}
