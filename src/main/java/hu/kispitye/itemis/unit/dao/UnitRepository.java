package hu.kispitye.itemis.unit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.unit.Unit;
import hu.kispitye.itemis.user.User;

public interface UnitRepository extends HibernateRepository<Unit>, JpaRepository<Unit, Long> {
    Unit findByUserAndNameIgnoreCase(User user, String name);
}
