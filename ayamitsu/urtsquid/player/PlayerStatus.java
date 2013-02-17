package ayamitsu.urtsquid.player;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerStatus {

	private byte heartCount;
	private boolean parasiteStat;

	public PlayerStatus() {}

	public void setHeartCount(byte b) {
		this.heartCount = b;
	}

	public byte getHeartCount() {
		return this.heartCount;
	}

	public byte getMaxHeartCount() {
		return 3;
	}

	public boolean isParasiteStat() {
		return this.parasiteStat;
	}

	public void setMountStat(boolean flag) {
		this.parasiteStat = flag;
	}

	public void readStatus(NBTTagCompound nbttagcompound) {
		this.heartCount = nbttagcompound.getByte("HeartCount");
		this.parasiteStat = nbttagcompound.getBoolean("MountStat");
	}

	public void writeStatus(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("HeartCount", this.heartCount);
		nbttagcompound.setBoolean("MountStat", this.parasiteStat);
	}

}
