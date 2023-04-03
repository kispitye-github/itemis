package hu.kispitye.itemis.repository;

import hu.kispitye.itemis.model.UserWithUnitsAndItems;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithUnitsAndItemsRepository extends JpaRepository<UserWithUnitsAndItems, Long> {}
