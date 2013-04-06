package ayamitsu.urtsquid.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * use on EntityPlayerSquidMP, for recovery item judgment
 */
public final class BreathRecoveryItemAPI {

	private static final List<BreathRecoveryItemHandler> handlers = new ArrayList<BreathRecoveryItemHandler>();

	public static void register(BreathRecoveryItemHandler handler) {
		handlers.add(handler);
	}

	public static void unregister(BreathRecoveryItemHandler handler) {
		handlers.remove(handler);
	}

	public static int getAmount(ItemStack itemStack) {
		for (BreathRecoveryItemHandler handler : handlers) {
			int amount = handler.amount(itemStack);

			if (amount > 0) {
				return amount;
			}
		}

		return 0;
	}

	public static interface BreathRecoveryItemHandler {

		int amount(ItemStack itemStack);

	}

	static
	{
		register(new BreathRecoveryItemHandler() {

			@Override
			public int amount(ItemStack itemStack) {
				return itemStack.itemID == Item.potion.itemID && itemStack.getItemDamage() == 0 ? 100 : 0;
			}
		});
	}
}
