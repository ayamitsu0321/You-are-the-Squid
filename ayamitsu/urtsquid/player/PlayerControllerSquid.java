package ayamitsu.urtsquid.player;

import ayamitsu.urtsquid.network.PacketHandler;
import ayamitsu.urtsquid.util.Reflector;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerSquid extends PlayerControllerMP {

	private final Minecraft mc;
	private final NetClientHandler netClientHandler;

	public PlayerControllerSquid(Minecraft mc, NetClientHandler handler) {
		super(mc, handler);
		System.out.println("PlayerControllerSquid");
		this.mc = mc;
		this.netClientHandler = handler;
	}

	@Override
	public boolean onPlayerRightClick(EntityPlayer player, World world, ItemStack itemStack, int x, int y, int z, int side, Vec3 hitVec) {
		return super.onPlayerRightClick(player, world, itemStack, x, y, z, side, hitVec);
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

		if (var1 != this.getCurrentPlayerItem())
		{
			this.setCurrentPlayerItem(var1);
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.getCurrentPlayerItem()));
		}
	}

	protected int getCurrentPlayerItem() {
		try {
			return ((Integer)Reflector.getPrivateValue(PlayerControllerMP.class, this, 12)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	protected void setCurrentPlayerItem(int i) {
		try {
			Reflector.setPrivateValue(PlayerControllerMP.class, this, 12, i);
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
