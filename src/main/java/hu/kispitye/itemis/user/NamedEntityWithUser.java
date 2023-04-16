package hu.kispitye.itemis.user;

import java.util.Objects;

import org.hibernate.annotations.NaturalId;

import hu.kispitye.itemis.dao.NamedEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEntityWithUser<T extends NamedEntityWithUser<T>> extends NamedEntity<NamedEntityWithUser<T>> {
	@ManyToOne(optional=false, fetch = FetchType.LAZY)
	@NaturalId
	private User user;

	public User getUser() {
		return user;
	}

	@SuppressWarnings("unchecked")
	public T setUser(User user) {
		this.user = user;
		return (T) this;
	}
	
	protected NamedEntityWithUser()  {}

	protected NamedEntityWithUser(User user, String name) {
		setUser(user);
		setName(name);
	}

	@Override
	public boolean equals(Object entity) {
		if (!(entity instanceof NamedEntityWithUser)) return false;
		if (entity==this) return true;
		@SuppressWarnings("unchecked")
		NamedEntityWithUser<T> entityWithUser=(NamedEntityWithUser<T>)entity;
		if (!getUser().equals(Objects.requireNonNull(entityWithUser.getUser()))) return false;
		return super.equals(entityWithUser);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUser(), getKey());
	}
}
