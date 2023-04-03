package hu.kispitye.itemis.service;

import java.math.BigDecimal;

import hu.kispitye.itemis.model.*;

public interface ItemService {
	Item createItem(UserWithUnitsAndItems user, String name, BigDecimal price);
	Item updateItem(Item item);
	void deleteItem(Item item);
	Item findItemByName(UserWithUnitsAndItems user, String name);
}
