package hu.kispitye.itemis.user;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;
import hu.kispitye.itemis.user.dao.UserRepository;

@DataJpaTest
@EntityScan(basePackageClasses = {User.class})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, HibernateRepository.class})
public class UserRepositoryTest extends HibernateRepositoryTest<UserRepository, User> {

	@Override
	protected User newEntity() {
		return new User().setPassword("pwd");
	}

	@Test
    void testUser() {
    	assertEquals(repository.count(), 0);
    	User user = new User("user","pwd");
    	repository.persist(user);
    	repository.flush();
    	User user2 = new User("user","pwd2");
    	repository.persist(user2);
    	Exception exception = assertThrows(DataIntegrityViolationException.class, () -> repository.flush());
    	assertTrue(exception.getMessage().contains("key violation"));
    	entityManager.clear();
    	assertEquals(repository.count(), 1);
    	user2 = repository.findByNameIgnoreCase("User");
    	assertEquals(user, user2);
    }
}
