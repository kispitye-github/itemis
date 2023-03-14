package hu.kispitye.itemis.service;

import java.math.BigDecimal;

import hu.kispitye.itemis.model.*;

public interface ItemService {
	Item createItem(User user, String name, BigDecimal price);
	Item updateItem(Item item);
	void deleteItem(Item item);
	Item findItemByName(User user, String name);
}
