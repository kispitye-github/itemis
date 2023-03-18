package hu.kispitye.itemis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.repository.UnitRepository;
import hu.kispitye.itemis.service.UnitService;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

	@Override
	public Unit createUnit(User user, String name, RomanNumeral numeral) {
		return unitRepository.save(new Unit(user, name, numeral));
	}

	@Override
	public Unit updateUnit(Unit unit) {
		return unitRepository.save(unit);
	}

	@Override
	public void deleteUnit(Unit unit) {
		unitRepository.delete(unit);
	}

	@Override
	public Unit findUnitByName(User user, String name) {
		return unitRepository.findByUserAndNameIgnoreCase(user, name);
	}

	@Override
	public Unit findUnitByRomanNumeral(User user, RomanNumeral numeral) {
		return unitRepository.findByUserAndNumeral(user, numeral);
	}

}
