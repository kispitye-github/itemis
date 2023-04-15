package hu.kispitye.itemis.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.user.User;

public interface UserRepository extends HibernateRepository<User>, JpaRepository<User, Long> {
    User findByNameIgnoreCase(String name);
}
