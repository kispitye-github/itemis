package hu.kispitye.itemis.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;

public abstract class NamedEntityTest<T extends NamedEntity<?>> {
	
	protected abstract T newEntity();
		
	@Test
	void testIdNameFields()  {
		T entity = newEntity();
		assertThat(entity.getName()).isNull();
		final String NAME = entity.getClass().getName();
		entity.setName(NAME);
		assertThat(entity.getName()).isEqualTo(NAME);
		assertThat(entity.getId()).isNull();
		final long id = System.currentTimeMillis();
		entity.setId(id);
		assertThat(entity.getId()).isEqualTo(id);
		testOtherFields(entity);
	}
		
	protected abstract void testOtherFields(T t);

	protected void testNotEquals(T entity1, T entity2) {
		assertThat(entity1).isEqualTo(entity1);
		assertThat(entity2).isEqualTo(entity2);
		assertThat(entity1).isNotEqualTo(entity2);
		assertThat(entity2).isNotEqualTo(entity1);
	}

	protected void testEquals(T entity1, T entity2) {
		assertThat(entity1).isEqualTo(entity1);
		assertThat(entity2).isEqualTo(entity2);
		assertThat(entity1).isEqualTo(entity2);
		assertThat(entity2).isEqualTo(entity1);
		assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
	}

	protected void testNameFields(T entity1, T entity2) {
		testEquals(entity1, entity2);
		entity1.setName(entity1.getName()+" ");
		testNotEquals(entity1, entity2);
		entity2.setName(entity1.getName().toUpperCase());
		testEquals(entity1, entity2);
		entity2.setName(entity1.getName());
	}
	
	protected void testFields(T entity1, T entity2) {
		testNameFields(entity1, entity2);
		testOtherFields(entity1, entity2);
		testNameFields(entity1, entity2);
	}
	
	protected abstract void testOtherFields(T entity1, T entity2);
	
	protected void testIdEquals(T entity1, T entity2) {
		testFields(entity1, entity2);
		entity1.setId(1l);
		testFields(entity1, entity2);
		entity2.setId(entity1.getId());
		testFields(entity1, entity2);
		entity2.setId(entity2.getId()+1);
		testNotEquals(entity1, entity2);
	}
	
	@Test
	protected void testEquals() {
		T entity1 = newEntity();
		assertThat(entity1).isNotEqualTo(null);
		T entity2 = newEntity();
		testIdEquals(entity1, entity2);
	}

	static class EntityTest extends NamedEntityTest<TestEntity> {
		@Override
		protected TestEntity newEntity()  {
			return new TestEntity();
		}

		@Override
		protected void testOtherFields(TestEntity t) {}

		@Override
		protected void testOtherFields(TestEntity entity1, TestEntity entity2) {}

	}

    @Entity
    static class TestEntity extends NamedEntity<TestEntity> {}


}
