package ayamitsu.urtsquid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerStatus {

	private byte heartCount;

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

	public void readStatus(NBTTagCompound nbttagcompound) throws IOException {
		this.heartCount = nbttagcompound.getByte("HeartCount");
	}

	public void writeStatus(NBTTagCompound nbttagcompound) throws IOException {
		nbttagcompound.setByte("HeartCount", this.heartCount);
	}
}
