package hu.kispitye.itemis.unit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.roman.RomanNumeral;
import hu.kispitye.itemis.unit.dao.UnitRepository;
import hu.kispitye.itemis.user.NamedEntityWithUserRepositoryTest;
import hu.kispitye.itemis.user.User;

@DataJpaTest
@EntityScan(basePackageClasses = {User.class, Unit.class})
@EnableJpaRepositories(basePackageClasses = {UnitRepository.class, HibernateRepository.class})
public class UnitRepositoryTest extends NamedEntityWithUserRepositoryTest<UnitRepository, Unit> {

    @Test
    void testUnit() {
    	assertEquals(repository.count(), 0);
    	User user = new User("user","pwd");
    	entityManager.persist(user);
    	Unit unit = new Unit(user, "unit", RomanNumeral.I);
    	repository.persist(unit);
    	repository.flush();
    	Unit unit2 = new Unit(user, "unit", RomanNumeral.V);
    	repository.persist(unit2);
    	Exception exception = assertThrows(DataIntegrityViolationException.class, () -> repository.flush());
    	assertTrue(exception.getMessage().contains("key violation"));
    	entityManager.clear();
    	assertEquals(repository.count(), 1);
    	unit2 = repository.findByUserAndNameIgnoreCase(user, "Unit");
    	assertEquals(unit, unit2);
    	unit.setName("unit2");
    	repository.update(unit);
    	repository.flush();
    	unit2 = repository.findByUserAndNameIgnoreCase(user, unit.getName());    	
    	assertEquals(unit, unit2);
    	assertEquals(unit2.getName(), "unit2");
    	unit.setNumeral(RomanNumeral.C);
    	repository.update(unit);
    	repository.flush();
    	unit2 = repository.findByUserAndNameIgnoreCase(user, unit.getName());    	
    	assertEquals(unit, unit2);
    	assertEquals(unit2.getNumeral(), RomanNumeral.C);
    	assertEquals(repository.count(), 1);
    	repository.delete(unit);
    	repository.flush();
    	assertEquals(repository.count(), 0);
    }

	@Override
	protected Unit newEntity(User user) {
		return new Unit(user, null, RomanNumeral.C);
	}
    
}
