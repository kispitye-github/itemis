package hu.kispitye.itemis.item.service;

import java.math.BigDecimal;

import hu.kispitye.itemis.item.Item;
import hu.kispitye.itemis.user.User;

public interface ItemService {
	Item createItem(User user, String name, BigDecimal price);
	Item updateItem(Item item);
	void deleteItem(Item item);
	Item findItemByName(User user, String name);
}
