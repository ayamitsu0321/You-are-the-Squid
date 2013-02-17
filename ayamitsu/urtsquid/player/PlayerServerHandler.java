package ayamitsu.urtsquid.player;

import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import net.minecraft.util.DamageSource;

public class PlayerServerHandler extends ServerPlayerBase {

	private byte heartCount;

	public PlayerServerHandler(ServerPlayerAPI api) {
		super(api);
	}

	public byte getHeartCount() {
		return this.heartCount;
	}

	@Override
	public void onDeath(DamageSource damage) {
		if (this.heartCount < 2) {
			heartCount++;
			this.player.setHealthField(this.player.getMaxHealth());
		} else {
			this.heartCount = (byte)0;
			this.player.superOnDeath(damage);
		}

		URTSquid.instance.playerStatus.setHeartCount(this.heartCount);
		PacketHandler.sendHeartStatusToClient(this.player);
	}

	@Override
	public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.heartCount = nbttagcompound.getByte("URTS_Heart");
		URTSquid.instance.playerStatus.setHeartCount(this.heartCount);
	}

	@Override
	public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("URTS_Heart", this.heartCount);
		URTSquid.instance.playerStatus.setHeartCount(this.heartCount);
	}
}
