package ayamitsu.urtsquid.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;

public class KeySimpleHandler extends cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler {

	public KeySimpleHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	public KeySimpleHandler(KeyBinding[] keyBindings) {
		super(keyBindings);
	}

	@Override
	public String getLabel() {
		return "URTSquid.key_simple";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding key, boolean tickEnd, boolean isRepeat) {
		if (tickEnd && this.canInputKey()) {
			if (key.keyDescription.equals("ToggleParasite")) {
				PacketHandler.sendSimpleKeyInputPacket(key);
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding key, boolean tickEnd) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	private boolean canInputKey() {
		return FMLClientHandler.instance().getClient().thePlayer != null && FMLClientHandler.instance().getClient().theWorld != null && FMLClientHandler.instance().getClient().currentScreen == null;
	}
}
