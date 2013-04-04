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

	public static boolean match(ItemStack itemStack) {
		for (BreathRecoveryItemHandler handler : handlers) {
			if (handler.match(itemStack)) {
				return true;
			}
		}

		return false;
	}

	public static interface BreathRecoveryItemHandler {
		boolean match(ItemStack itemStack);
	}

	static
	{
		register(new BreathRecoveryItemHandler() {

			@Override
			public boolean match(ItemStack itemStack) {
				// water bottle
				return itemStack.itemID == Item.potion.itemID && itemStack.getItemDamage() == 0;
			}
		});
	}
}
