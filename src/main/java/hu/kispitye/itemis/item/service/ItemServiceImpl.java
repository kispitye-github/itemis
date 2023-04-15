package hu.kispitye.itemis.item.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.kispitye.itemis.item.Item;
import hu.kispitye.itemis.item.dao.ItemRepository;
import hu.kispitye.itemis.user.User;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
	private ItemRepository itemRepository;

	@Override
	public Item createItem(User user, String name, BigDecimal price) {
		return itemRepository.persist(new Item(user, name, price));
	}

	@Override
	public Item updateItem(Item item) {
		return itemRepository.update(item);
	}

	@Override
	public void deleteItem(Item item) {
		itemRepository.delete(item);
		itemRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public Item findItemByName(User user, String name) {
		return itemRepository.findByUserAndNameIgnoreCase(user, name);
	}
}
