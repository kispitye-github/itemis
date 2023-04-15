package hu.kispitye.itemis.user;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;

@EntityScan(basePackageClasses = {User.class, NamedEntityWithUser.class})
@EnableJpaRepositories(basePackageClasses = {HibernateRepository.class})
public abstract class NamedEntityWithUserRepositoryTest<R extends HibernateRepository<T>, T extends NamedEntityWithUser> extends HibernateRepositoryTest<R, T> {

	protected abstract T newEntity(User user);

	@Override
	protected final T newEntity() {
		User user = new User("newEntity", "pwd");
		entityManager.persist(user);
		T entity = newEntity(user);
		entity.setUser(user);
		return entity;
	}


}
