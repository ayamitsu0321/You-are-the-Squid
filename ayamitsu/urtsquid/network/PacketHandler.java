package ayamitsu.urtsquid.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
			this.handlerKeyInput(manager, packet, player);
		} else if (packet.channel.equals(STATUS_CHANNEL)) {
			this.recieveStatusPacket(manager, packet, player);
		}
	}

	private void handlerKeyInput(INetworkManager manager, Packet250CustomPayload packet, Player player) {
	}

	public static void recieveStatusPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			NBTTagCompound nbttagcompound = CompressedStreamTools.decompress(packet.data);
			URTSquid.instance.playerStatus.readStatus(nbttagcompound);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendHeartStatusToClient(EntityPlayerMP player) {
		PlayerServerHandler playerHandler = (PlayerServerHandler)player.serverPlayerAPI.getServerPlayerBase("URTSquid");

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
