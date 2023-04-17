package hu.kispitye.itemis.item;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.BootstrapContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;
import hu.kispitye.itemis.item.dao.ItemRepository;
import hu.kispitye.itemis.user.User;

@DataJpaTest
@EntityScan(basePackageClasses = {User.class, Item.class})
@EnableJpaRepositories(basePackageClasses = {ItemRepository.class, HibernateRepository.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@BootstrapWith(ItemRepositoryTest.Hack.class)
public class ItemRepositoryTest extends HibernateRepositoryTest<ItemRepository, Item> {

	static class Hack implements TestContextBootstrapper {
		private DefaultTestContextBootstrapper delegate = new DefaultTestContextBootstrapper(); 
		public Hack() {
			
		}
			@Override
			public
			List<TestExecutionListener> getTestExecutionListeners() {
				List<TestExecutionListener> listeners = delegate.getTestExecutionListeners();
listeners.stream().forEach(listener -> System.out.println("!!!!HACK!!!!"+listener));
				return listeners;
		}

			@Override
			public void setBootstrapContext(BootstrapContext bootstrapContext) {
				delegate.setBootstrapContext(bootstrapContext);
			}

			@Override
			public BootstrapContext getBootstrapContext() {
				return delegate.getBootstrapContext();
			}

			@Override
			public TestContext buildTestContext() {
				return delegate.buildTestContext();
			}

			@Override
			public MergedContextConfiguration buildMergedContextConfiguration() {
				return delegate.buildMergedContextConfiguration();
			}
	}
	
	@Test
    void testItem() {
    	assertEquals(repository.count(), 0);
    	User user = new User("user","pwd");
    	entityManager.persist(user);
    	Item item = new Item(user, "item", BigDecimal.ONE);
    	repository.persist(item);
    	repository.flush();
    	Item item2 = new Item(user, "item", BigDecimal.TWO);
    	repository.persist(item2);
    	Exception exception = assertThrows(DataIntegrityViolationException.class, () -> repository.flush());
    	assertTrue(exception.getMessage().contains("key violation"));
    	entityManager.clear();
    	assertEquals(repository.count(), 1);
    	item2 = repository.findByUserAndNameIgnoreCase(user, "Item");
    	assertEquals(item, item2);
    	item.setPrice(BigDecimal.TEN);
    	repository.update(item);
    	repository.flush();
    	item2 = repository.findByUserAndNameIgnoreCase(user, item.getName());
    	assertEquals(item, item2);
    	assertTrue(item.getPrice().compareTo(BigDecimal.TEN)==0);
    	item.setName("item2");
    	repository.update(item);
    	repository.flush();
    	item2 = repository.findByUserAndNameIgnoreCase(user, item.getName());
    	assertEquals(item, item2);
    	assertEquals(item.getName(),"item2");
    	assertEquals(repository.count(), 1);
    	repository.delete(item);
    	repository.flush();
    	assertEquals(repository.count(), 0);
    }

	@Override
	protected Item newEntity() {
    	User user = new User("user","pwd");
    	entityManager.persist(user);
		return new Item(user, null, BigDecimal.ONE);
	}
   
}