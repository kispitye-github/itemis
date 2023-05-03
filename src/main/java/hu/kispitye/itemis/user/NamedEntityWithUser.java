package hu.kispitye.itemis.user;

import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import hu.kispitye.itemis.dao.NamedEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@MappedSuperclass
@Table(uniqueConstraints={@UniqueConstraint(columnNames ={NamedEntityWithUser.USER_ID_ATTRIBUTE, NamedEntity.NAME_ATTRIBUTE})})
@AttributeOverride(name = NamedEntity.NAME_ATTRIBUTE, column = @Column(name = NamedEntity.NAME_ATTRIBUTE, nullable = false, unique = false, length = 42))
@DynamicUpdate
public abstract class NamedEntityWithUser<T extends NamedEntityWithUser<T>> extends NamedEntity<NamedEntityWithUser<T>> {

	public static final String USER_ID_ATTRIBUTE = "user_id";
	
	@ManyToOne(optional=false, fetch = FetchType.LAZY)
	@NaturalId
	@JoinColumn(name = USER_ID_ATTRIBUTE)
	User user;

	public User getUser() {
		return user;
	}

	public NamedEntityWithUser<T> setUser(User user) {
		this.user = user;
		return this;
	}
	
	protected NamedEntityWithUser()  {}

	protected NamedEntityWithUser(User user, String name) {
		setUser(user);
		setName(name);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NamedEntityWithUser<?> entityWithUser)) return false;
		if (entityWithUser==this) return true;
		if (!getUser().equals(Objects.requireNonNull(entityWithUser.getUser()))) return false;
		return super.equals(entityWithUser);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUser(), getKey());
	}
}
