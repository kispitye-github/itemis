package hu.kispitye.itemis.item;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import hu.kispitye.itemis.user.NamedEntityWithUserTest;

public class ItemTest extends NamedEntityWithUserTest<Item> {
	
	private static final BigDecimal PRICE = BigDecimal.ONE;
	private static final BigDecimal PRICE2 = BigDecimal.TWO;

	@Override
	protected Item newEntity() {
		return new Item();
	}
	
	@Override
	protected void testOtherFields(Item item) {
		assertThat(item.getPrice()).isNull();
		item.setPrice(PRICE);
		assertThat(item.getPrice()).isEqualByComparingTo(PRICE);
	}

	@Override
	protected void testOtherFields(Item item1, Item item2) {
		item1.setPrice(PRICE);
		item2.setPrice(PRICE2);
		assertThat(item1.getPrice()).isNotEqualByComparingTo(item2.getPrice());
	}
	
}
