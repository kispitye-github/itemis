package hu.kispitye.itemis.user;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;

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
