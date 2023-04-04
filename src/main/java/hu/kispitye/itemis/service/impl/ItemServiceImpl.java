package hu.kispitye.itemis.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.kispitye.itemis.model.*;
import hu.kispitye.itemis.repository.ItemRepository;
import hu.kispitye.itemis.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
	private ItemRepository itemRepository;

	@Override
	@Transactional
	public Item createItem(UserWithUnitsAndItems user, String name, BigDecimal price) {
		return itemRepository.persist(new Item(user, name, price));
	}

	@Override
	@Transactional
	public Item updateItem(Item item) {
		return itemRepository.update(item);
	}

	@Override
	@Transactional
	public void deleteItem(Item item) {
		itemRepository.delete(item);
		itemRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public Item findItemByName(UserWithUnitsAndItems user, String name) {
		return itemRepository.findByUserAndNameIgnoreCase(user, name);
	}
}
