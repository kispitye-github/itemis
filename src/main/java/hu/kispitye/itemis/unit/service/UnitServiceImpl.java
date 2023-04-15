package hu.kispitye.itemis.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.kispitye.itemis.roman.*;
import hu.kispitye.itemis.unit.Unit;
import hu.kispitye.itemis.unit.dao.UnitRepository;
import hu.kispitye.itemis.user.User;

@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

	@Override
	public Unit createUnit(User user, String name, RomanNumeral numeral) {
		return unitRepository.persist(new Unit(user, name, numeral));
	}

	@Override
	public Unit updateUnit(Unit unit) {
		return unitRepository.update(unit);
	}

	@Override
	public void deleteUnit(Unit unit) {
		unitRepository.delete(unit);
		unitRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public Unit findUnitByName(User user, String name) {
		return unitRepository.findByUserAndNameIgnoreCase(user, name);
	}

}
