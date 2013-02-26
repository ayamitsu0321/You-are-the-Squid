package ayamitsu.urtsquid.player;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerStatus {

	private boolean parasiteStat;

	public PlayerStatus() {}

	public byte getMaxHeartCount() {
		return 3;
	}

	public boolean isParasiteStat() {
		return this.parasiteStat;
	}

	public void setParasiteStat(boolean flag) {
		this.parasiteStat = flag;
	}

	public void readStatus(NBTTagCompound nbttagcompound) {
		this.parasiteStat = nbttagcompound.getBoolean("MountStat");
	}

	public void writeStatus(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("MountStat", this.parasiteStat);
	}

}
