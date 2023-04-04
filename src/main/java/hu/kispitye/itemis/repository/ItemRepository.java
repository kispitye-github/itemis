package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.*;

public interface ItemRepository extends HibernateRepository<Item>, JpaRepository<Item, Long> {
    Item findByUserAndNameIgnoreCase(UserWithUnitsAndItems user, String name);
}
