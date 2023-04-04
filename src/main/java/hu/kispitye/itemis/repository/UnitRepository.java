package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.*;

public interface UnitRepository extends HibernateRepository<Unit>, JpaRepository<Unit, Long> {
    Unit findByUserAndNameIgnoreCase(UserWithUnitsAndItems user, String name);

    Unit findByUserAndNumeral(UserWithUnitsAndItems user, RomanNumeral numeral);
}
