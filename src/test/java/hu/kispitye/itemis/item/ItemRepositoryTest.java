package hu.kispitye.itemis.item;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.dao.HibernateRepositoryTest;
import hu.kispitye.itemis.item.dao.ItemRepository;
import hu.kispitye.itemis.user.User;

@DataJpaTest
@EntityScan(basePackageClasses = {User.class, Item.class})
@EnableJpaRepositories(basePackageClasses = {ItemRepository.class, HibernateRepository.class})
@TestExecutionListeners({ItemRepositoryTest.Hack.class, DependencyInjectionTestExecutionListener.class,/*TransactionalTestExecutionListener.class*/})
public class ItemRepositoryTest extends HibernateRepositoryTest<ItemRepository, Item> {

	static class Hack extends TransactionalTestExecutionListener {
		public Hack()  {}

		@Override
		public void beforeTestMethod(final TestContext testContext) throws Exception {
			try  {
				super.beforeTestMethod(testContext);
			} catch (Exception e)  {
				System.err.println("!!!!HACK!!!!"+e);
				afterTestMethod(testContext);
				super.beforeTestMethod(testContext);
			}
			

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