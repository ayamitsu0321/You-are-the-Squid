package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import ayamitsu.util.reflect.Reflector;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;

public class TransfomerNetClientHandler extends TransformerBase {

	private static final String NETCLIENTHANDLER_CLASS_NAME = "net.minecraft.client.multiplayer.NetClientHandler";// NetClientHandler

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (FMLLaunchHandler.side() != Side.CLIENT || !transformedName.equals(NETCLIENTHANDLER_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformNetClientHandler(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform NetClientHandler", e);
		}
	}

	private byte[] transformNetClientHandler(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);

		String targetMethodName = Reflector.isRenameTable() ? "handleLogin" : "func_72455_a";// handleLogin
		String targetMethodDesc = "(Lnet/minecraft/network/packet/Packet1Login;)V";// void (Packet1Login)
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {
				AbstractInsnNode aiNode = insnList[i];

				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (tiNode.getOpcode() == NEW && "net/minecraft/client/multiplayer/PlayerControllerMP".equals(this.map(tiNode.desc))) {
						tiNode.desc = "ayamitsu/urtsquid/player/PlayerControllerSquid";
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && miNode.name.equals("<init>") && ("net/minecraft/client/multiplayer/PlayerControllerMP").equals(this.map(miNode.owner)) && ("(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/NetClientHandler;)V").equals(this.mapMethodDesc(miNode.desc))) {
						//miNode.owner = "ayamitsu/urtsquid/player/PlayerControllerSquid";
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/PlayerControllerSquid", new String(miNode.name), new String(miNode.desc)));
					}
				}
			}

			bytes = this.decode(cNode);
		}

		return bytes;
	}

}
