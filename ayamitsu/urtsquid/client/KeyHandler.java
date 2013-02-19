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
			if (key.keyDescription.equals("ToggleParasite")) {
				PacketHandler.sendSimpleKeyInputPacket(key);
			} else if (key.keyDescription.equals("DoParasite")) {
				this.handleDoParasiteKey();
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

	private void handleDoParasiteKey() {
		if (!URTSquid.instance.playerStatus.isParasiteStat() || FMLClientHandler.instance().getClient().thePlayer.inventory.getCurrentItem() != null) {
			return;
		}

		MovingObjectPosition objectMouseOver = FMLClientHandler.instance().getClient().objectMouseOver;

		if (objectMouseOver != null && objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
			if (objectMouseOver.entityHit instanceof EntityLiving) {
				PacketHandler.sendParasiteMobPacket(objectMouseOver.entityHit.entityId);
			}
		}
	}
}
