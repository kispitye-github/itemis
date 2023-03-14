package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.*;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Unit findByUserAndNameIgnoreCase(User user, String name);

    Unit findByUserAndNumeral(User user, RomanNumeral numeral);
}
