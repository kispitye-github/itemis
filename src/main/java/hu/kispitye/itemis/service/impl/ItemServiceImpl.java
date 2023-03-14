package hu.kispitye.itemis.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.repository.ItemRepository;
import hu.kispitye.itemis.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
	
	@Override
	public Item createItem(User user, String name, BigDecimal price) {
		return itemRepository.save(new Item(user, name, price));
	}

	@Override
	public Item updateItem(Item item) {
		return itemRepository.save(item);
	}

	@Override
	public void deleteItem(Item item) {
		itemRepository.delete(item);
	}

	@Override
	public Item findItemByName(User user, String name) {
		return itemRepository.findByUserAndNameIgnoreCase(user, name);
	}
}
