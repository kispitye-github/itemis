package hu.kispitye.itemis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.kispitye.itemis.model.UserWithUnitsAndItems;

public interface UserWithUnitsAndItemsRepository extends HibernateRepository<UserWithUnitsAndItems>, JpaRepository<UserWithUnitsAndItems, Long> {}
