package ayamitsu.urtsquid.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import net.minecraft.util.DamageSource;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;

public class PlayerServerHandler extends ServerPlayerBase {

	public PlayerServerHandler(ServerPlayerAPI api) {
		super(api);
	}

	@Override
	public void onDeath(DamageSource damage) {
		if (URTSquid.instance.playerStatus.getHeartCount() < URTSquid.instance.playerStatus.getMaxHeartCount()) {
			URTSquid.instance.playerStatus.setHeartCount((byte)(1 + URTSquid.instance.playerStatus.getHeartCount()));
			this.player.setHealthField(this.player.getMaxHealth());
		} else {
			URTSquid.instance.playerStatus.setHeartCount((byte)0);
			this.player.superOnDeath(damage);
		}

		PacketHandler.sendStatusToClient(this.player);
	}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block) {
		System.out.println("foo");
		float var0 = this.player.localGetCurrentPlayerStrVsBlock(block);

		if (this.player.isInsideOfMaterial(Material.water)) {
			var0 *= 5.0F;
		}

		return var0;
	}

	@Override
	public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound) {
		URTSquid.instance.playerStatus.readStatus(nbttagcompound);
	}

	@Override
	public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound) {
		URTSquid.instance.playerStatus.writeStatus(nbttagcompound);
	}

}
