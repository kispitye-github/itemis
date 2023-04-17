package hu.kispitye.itemis.dao;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hu.kispitye.itemis.dao.NamedEntityTest.TestEntity;
import jakarta.transaction.Transactional;

@Transactional
public abstract class HibernateRepositoryTest<R extends HibernateRepository<T>, T extends NamedEntity<?>> {
	
    @Autowired
	protected R repository;

    @Autowired
    protected TestEntityManager entityManager;
    
    @Test
    void testAutowired() {
    	assertThat(repository).isNotNull();
    	assertThat(entityManager).isNotNull();
    }

	protected abstract T newEntity();
	
	protected Class<? extends Exception> expectedNotNullableException() {
		return DataIntegrityViolationException.class;
	}

    @Test
    void testNotNullableColumn() {
    	T entity = newEntity();
    	entity.setName(null);
    	assertThat(entity.getName()).isNull();
    	Exception exception = assertThrows(expectedNotNullableException(), () -> repository.persist(entity));
    	assertTrue(exception.getMessage().contains("not-null property"));
    }

    @Test
    void testUniqueColumn() {
    	T entity1 = newEntity(); 
    	entity1.setName(getClass().getSimpleName());
    	repository.persist(entity1);
    	T entity2 = newEntity();
    	entity2.setName(entity1.getName());
    	repository.persist(entity2);
    	Exception exception = assertThrows(ConstraintViolationException.class, () -> entityManager.flush());
    	assertTrue(exception.getMessage().contains("key violation"));
    }

    protected T testResult(boolean withId, T entity1, T entity2) {
    	entityManager.flush();
        assertThat(entity1).isEqualTo(entity2);
    	if (withId) assertThat(entity1.getId()).isNotNull();
    	else assertThat(entity1.getId()).isNull();
    	assertThat(entity2.getId()).isNotNull();
        @SuppressWarnings("unchecked")
		T entity = (T) entityManager.find(entity1.getClass(), entity2.getId());
        assertThat(entity).isEqualTo(entity1);
        return entity;
    }
    
    @Test
    void testPersist() {
    	T entity =  newEntity();
    	entity.setName("perist");
    	testResult(true, entity, repository.persist(entity));
    }

    @Test
    void testMerge() {
    	T entity = newEntity();
    	entity.setName("merge");
    	testResult(false,entity, repository.merge(entity));
    }

    @Test
    void testUpdate() {
    	T entity = newEntity();
    	entity.setName("update");
    	repository.persist(entity);
    	entity.setName(getClass().getSimpleName());
    	T entityp = testResult(true, entity, repository.update(entity));
    	assertThat(entityp.getName()).isEqualTo(getClass().getSimpleName());
    }

    protected Class<? extends Exception> expectedNullSaveException() {
    	return UnsupportedOperationException.class;
    }
    
    @Test
    @SuppressWarnings("deprecation")
    void testFindAllAndSave() {
        assertThrows(UnsupportedOperationException.class, () -> repository.findAll());
    	T entity = newEntity();        
        assertThrows(expectedNullSaveException(), () -> repository.save(null));
        assertThrows(UnsupportedOperationException.class, () -> repository.save(entity));
        assertThrows(UnsupportedOperationException.class, () -> repository.saveAndFlush(entity));
        assertThrows(UnsupportedOperationException.class, () -> repository.saveAll(Arrays.asList(entity)));
        assertThrows(UnsupportedOperationException.class, () -> repository.saveAllAndFlush(Arrays.asList(entity)));
    }
    
    @DataJpaTest
    @EntityScan(basePackageClasses = {TestEntity.class})
    @EnableJpaRepositories(basePackageClasses = {HibernateRepository.class})
    static class TestEntityTest extends HibernateRepositoryTest<HibernateRepository<TestEntity>, TestEntity> { 

    	@Override
    	protected Class<? extends Exception> expectedNullSaveException() {
        	return NullPointerException.class;
        }
        
    	@Override
    	protected Class<? extends Exception> expectedNotNullableException() {
    		return PropertyValueException.class;
    	}

		@Override
		protected TestEntity newEntity() {
			return new TestEntity();
		}

		@Configuration
		static class Config  {
	        @Bean
		    HibernateRepository<TestEntity> getRepository() {
		    	return new HibernateRepositoryImpl<TestEntity>() {
			    	@Override
		    	    public <S extends TestEntity> S save(S entity) {
			    		if (entity==null) throw new NullPointerException();
		    	    	return super.save(entity);
		    	    }
	
		    	};
		    }
	    }
    }
}
