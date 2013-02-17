package ayamitsu.urtsquid.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ayamitsu.urtsquid.network.PacketHandler;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
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
		if (tickEnd && this.canInputKey()) {
			PacketHandler.sendKeyInputPacket(key);
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding key, boolean tickEnd) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	private boolean canInputKey() {
		return FMLClientHandler.instance().getClient().theWorld != null && FMLClientHandler.instance().getClient().currentScreen == null;
	}
}
