package ayamitsu.urtsquid.client;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.common.TickType;

public class KeyHandler extends cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler {

	public KeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	public KeyHandler(KeyBinding[] keyBindings) {
		super(keyBindings);
	}

	@Override
	public String getLabel() {
		return "URTSquid.key";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding key, boolean tickEnd, boolean isRepeat) {

	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding key, boolean tickEnd) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
