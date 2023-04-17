package hu.kispitye.itemis.user;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestExecutionListeners;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;

@EntityScan(basePackageClasses = {User.class, NamedEntityWithUser.class})
@EnableJpaRepositories(basePackageClasses = {HibernateRepository.class})
@TestExecutionListeners(listeners = {}, inheritListeners = false, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class NamedEntityWithUserRepositoryTest<R extends HibernateRepository<T>, T extends NamedEntityWithUser<T>> extends HibernateRepositoryTest<R, T> {

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
