package ayamitsu.urtsquid.network;

import java.io.IOException;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.player.PlayerServerHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public static final String KEY_CHANNEL = "urts.key";
	public static final String STATUS_CHANNEL = "urts.status";

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(KEY_CHANNEL)) {
			this.recieveKeyInputPacket(manager, packet, player);
		} else if (packet.channel.equals(STATUS_CHANNEL)) {
			this.recieveStatusPacket(manager, packet, player);
		}
	}

	private static void recieveKeyInputPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		NBTTagCompound nbttagcompound = null;

		try {
			nbttagcompound = CompressedStreamTools.decompress(packet.data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (nbttagcompound == null) {
			return;
		}

		if (nbttagcompound.hasKey("ToggleParasite")) {
			URTSquid.instance.playerStatus.setMountStat(!URTSquid.instance.playerStatus.isParasiteStat());
			((EntityPlayer)player).addChatMessage("toggle parasite stats:" + URTSquid.instance.playerStatus.isParasiteStat());
		}
	}

	public static void sendKeyInputPacket(KeyBinding ... arrayOfKey) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		KeyBinding keyBinding;

		for (int i = 0; i < arrayOfKey.length; i++) {
			keyBinding = arrayOfKey[i];
			nbttagcompound.setBoolean(keyBinding.keyDescription, keyBinding.pressed);
		}

		if (!nbttagcompound.hasNoTags()) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = KEY_CHANNEL;

			try {
				byte[] bytes = CompressedStreamTools.compress(nbttagcompound);
				packet.data = bytes;
				packet.length = bytes.length;
			} catch (IOException e) {
				e.printStackTrace();
			}

			PacketDispatcher.sendPacketToServer(packet);
		}
	}

	private static void recieveStatusPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			NBTTagCompound nbttagcompound = CompressedStreamTools.decompress(packet.data);
			URTSquid.instance.playerStatus.readStatus(nbttagcompound);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendStatusToClient(EntityPlayerMP player) {
		PlayerServerHandler playerHandler = (PlayerServerHandler)player.serverPlayerAPI.getServerPlayerBase("URTSquid.server");

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = STATUS_CHANNEL;

		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			URTSquid.instance.playerStatus.writeStatus(nbttagcompound);
			byte[] bytes = CompressedStreamTools.compress(nbttagcompound);
			packet.data = bytes;
			packet.length = bytes.length;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		PacketDispatcher.sendPacketToPlayer(packet, (Player)player);
	}

}
