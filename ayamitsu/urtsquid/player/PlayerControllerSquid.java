package ayamitsu.urtsquid.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.world.World;
import ayamitsu.urtsquid.network.PacketHandler;
import ayamitsu.util.reflect.Reflector;

public class PlayerControllerSquid extends PlayerControllerMP {

	private final Minecraft mc;
	private final NetClientHandler netClientHandler;

	public PlayerControllerSquid(Minecraft mc, NetClientHandler handler) {
		super(mc, handler);
		this.mc = mc;
		this.netClientHandler = handler;
	}

	@Override
	public EntityClientPlayerMP func_78754_a(World par1World) {
		return new EntityPlayerSquidSP(this.mc, par1World, this.mc.func_110432_I(), this.netClientHandler);
	}

	@Override
	public boolean func_78768_b(EntityPlayer player, Entity entity) {
		if (this.handleDoParasiteKey(entity)) {
			this.syncCurrentPlayItem();
			return true;
		}

		return super.func_78768_b(player, entity);
	}

	private void syncCurrentPlayItem() {
		int var1 = this.mc.thePlayer.inventory.currentItem;

		if (var1 != this.getCurrentPlayerItem()) {
			this.setCurrentPlayerItem(var1);
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.getCurrentPlayerItem()));
		}
	}

	protected int getCurrentPlayerItem() {// currentPlayerItem
		try {
			return ((Integer)Reflector.getPrivateValue(PlayerControllerMP.class, this, 11)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	protected void setCurrentPlayerItem(int i) {// currentPlayerItem
		try {
			Reflector.setPrivateValue(PlayerControllerMP.class, this, 11, i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean handleDoParasiteKey(Entity entity) {
		if (!((EntityPlayerSquidSP)this.mc.thePlayer).playerStatus.isParasiteStat() || this.mc.thePlayer.inventory.getCurrentItem() != null) {
			return false;
		}

		if (entity instanceof EntityLiving) {
			PacketHandler.sendParasiteMobPacket(entity.entityId);
			return true;
		}

		return false;
	}

}
