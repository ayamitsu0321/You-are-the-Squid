package ayamitsu.urtsquid.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.player.EntityPlayerSquidMP;
import ayamitsu.urtsquid.player.EntityPlayerSquidSP;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public static final String KEY_CHANNEL = "urts.key";
	public static final String STATUS_CHANNEL = "urts.status";
	public static final String PARASITE_CHANNEL = "urts.parasite";

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(KEY_CHANNEL)) {
			this.recieveKeyInputPacket(manager, packet, player);
		} else if (packet.channel.equals(STATUS_CHANNEL)) {
			this.recieveStatusPacket(manager, packet, player);
		} else if (packet.channel.equals(PARASITE_CHANNEL)) {
			this.recieveParasiteMobPacket(manager, packet, player);
		}
	}

	private static void recieveParasiteMobPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			int entityId = dis.readInt();
			EntityLiving living = null;

			for (Object obj : ((EntityPlayerMP)player).worldObj.loadedEntityList) {
				if (obj instanceof EntityLiving && ((EntityLiving)obj).entityId == entityId) {
					living = (EntityLiving)obj;
					break;
				}
			}

			if (living != null) {
				((EntityPlayerMP)player).mountEntity(living);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendParasiteMobPacket(int entityId) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(entityId);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (dos.size() > 0) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = PARASITE_CHANNEL;
			packet.data = baos.toByteArray();
			packet.length = dos.size();
			PacketDispatcher.sendPacketToServer(packet);
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
			((EntityPlayerSquidMP)player).playerStatus.setParasiteStat(nbttagcompound.getBoolean("ToggleParasite"));
			((EntityPlayer)player).addChatMessage("toggle parasite stats:" + ((EntityPlayerSquidMP)player).playerStatus.isParasiteStat());
		}
	}

	public static void sendSimpleKeyInputPacket(KeyBinding ... arrayOfKey) {
		((EntityPlayerSquidSP)FMLClientHandler.instance().getClient().thePlayer).playerStatus.setParasiteStat(!((EntityPlayerSquidSP)FMLClientHandler.instance().getClient().thePlayer).playerStatus.isParasiteStat());
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		KeyBinding keyBinding;

		for (int i = 0; i < arrayOfKey.length; i++) {
			keyBinding = arrayOfKey[i];

			if (keyBinding.keyDescription.equals("ToggleParasite")) {
				nbttagcompound.setBoolean(keyBinding.keyDescription, ((EntityPlayerSquidSP)FMLClientHandler.instance().getClient().thePlayer).playerStatus.isParasiteStat());
			} else {
				nbttagcompound.setBoolean(keyBinding.keyDescription, keyBinding.pressed);
			}
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

			if (player instanceof EntityPlayerSquidSP) {
				((EntityPlayerSquidSP)player).playerStatus.readStatus(nbttagcompound);
			} else {
				((EntityPlayerSquidMP)player).playerStatus.readStatus(nbttagcompound);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendStatusToClient(EntityPlayerMP player) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = STATUS_CHANNEL;

		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			((EntityPlayerSquidMP)player).playerStatus.writeStatus(nbttagcompound);
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
