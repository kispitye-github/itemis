package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.*;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByUserAndNameIgnoreCase(User user, String name);
}
