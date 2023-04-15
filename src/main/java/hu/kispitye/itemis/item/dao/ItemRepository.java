package hu.kispitye.itemis.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.item.Item;
import hu.kispitye.itemis.user.User;

public interface ItemRepository extends HibernateRepository<Item>, JpaRepository<Item, Long> {
    Item findByUserAndNameIgnoreCase(User user, String name);
}
