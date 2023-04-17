package hu.kispitye.itemis.item;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.item.dao.ItemRepository;
import hu.kispitye.itemis.user.NamedEntityWithUserRepositoryTest;
import hu.kispitye.itemis.user.User;

@EntityScan(basePackageClasses = {User.class, Item.class})
@EnableJpaRepositories(basePackageClasses = {ItemRepository.class, HibernateRepository.class})
public class ItemRepositoryTest extends NamedEntityWithUserRepositoryTest<ItemRepository, Item> {

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
	protected Item newEntity(User user) {
		return new Item(user, null, BigDecimal.ONE);
	}
   
}