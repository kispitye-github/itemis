package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.User;

public interface UserRepository extends HibernateRepository<User>, JpaRepository<User, Long> {
    User findByNameIgnoreCase(String name);
}
