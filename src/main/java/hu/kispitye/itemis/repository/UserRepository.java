package hu.kispitye.itemis.repository;

import hu.kispitye.itemis.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.name = ?1")
    User findByName(String name);

}