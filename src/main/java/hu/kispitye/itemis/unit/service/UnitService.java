package hu.kispitye.itemis.unit.service;

import hu.kispitye.itemis.roman.*;
import hu.kispitye.itemis.unit.Unit;
import hu.kispitye.itemis.user.User;

public interface UnitService {
	Unit createUnit(User user, String name, RomanNumeral numeral);
	Unit updateUnit(Unit unit);
	void deleteUnit(Unit unit);
	Unit findUnitByName(User user, String name);
}
