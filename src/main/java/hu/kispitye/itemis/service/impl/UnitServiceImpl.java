package hu.kispitye.itemis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.repository.UnitRepository;
import hu.kispitye.itemis.service.UnitService;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

	@Override
	@Transactional
	public Unit createUnit(UserWithUnitsAndItems user, String name, RomanNumeral numeral) {
		return unitRepository.persist(new Unit(user, name, numeral));
	}

	@Override
	@Transactional
	public Unit updateUnit(Unit unit) {
		return unitRepository.update(unit);
	}

	@Override
	@Transactional
	public void deleteUnit(Unit unit) {
		unitRepository.delete(unit);
		unitRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public Unit findUnitByName(UserWithUnitsAndItems user, String name) {
		return unitRepository.findByUserAndNameIgnoreCase(user, name);
	}

	@Override
	@Transactional(readOnly = true)
	public Unit findUnitByRomanNumeral(UserWithUnitsAndItems user, RomanNumeral numeral) {
		return unitRepository.findByUserAndNumeral(user, numeral);
	}

}
