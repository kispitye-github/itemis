package hu.kispitye.itemis.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import hu.kispitye.itemis.dao.NamedEntityTest;

public abstract class NamedEntityWithUserTest<T extends NamedEntityWithUser<T>> extends NamedEntityTest<T> {

	@Test
	protected void testEqualsWithUser() {
		T entity1 = newEntity();
		assertThat(entity1).isNotEqualTo(null);
		T entity2 = newEntity();
		entity2.setUser(new User("Name", "Pwd"));
		assertThrows(NullPointerException.class, () -> entity1.equals(entity2));
		assertThrows(NullPointerException.class, () -> entity2.equals(entity1));
		entity1.setUser(new User(entity2.getUser().getUsername().toLowerCase(), entity2.getUser().getPassword().toLowerCase()));
		assertThat(entity1.getUser()).isEqualTo(entity2.getUser());
		assertThat(entity1.getUser().getId()).isNull();
		assertThat(entity2.getUser().getId()).isNull();
		testFields(entity1, entity2);
		entity1.getUser().setId(1l);
		assertThat(entity1.getUser()).isEqualTo(entity2.getUser());
		testFields(entity1, entity2);
		entity2.getUser().setId(2l);
		assertThat(entity2.getUser()).isNotEqualTo(entity1.getUser());
		testNotEquals(entity1, entity2);
	}
	
	@Override
	protected void testIdEquals(T entity1, T entity2) {
		entity1.setUser(new User(getClass().getSimpleName(),""));
		entity1.getUser().setId(0l);
		entity2.setUser(entity1.getUser());
		assertThat(entity1.getUser()).isEqualTo(entity2.getUser());
		super.testIdEquals(entity1, entity2);
	}

	static class TestEntityTest extends NamedEntityWithUserTest<TestEntityWithUser> {

		@Override
		protected TestEntityWithUser newEntity() {
			return new TestEntityWithUser();
		}

		@Override
		protected void testOtherFields(TestEntityWithUser t) {}

		@Override
		protected void testOtherFields(TestEntityWithUser entity1, TestEntityWithUser entity2) {}
		
	}

	static class TestEntityWithUser extends NamedEntityWithUser<TestEntityWithUser> {}
}
