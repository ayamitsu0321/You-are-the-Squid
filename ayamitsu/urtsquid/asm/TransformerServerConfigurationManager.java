package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerServerConfigurationManager extends TransformerBase {

	private static final String SERVERCONFIGURATIONMANAGER_CLASS_NAME = "net.minecraft.server.management.ServerConfigurationManager";//"ServerConfigurationManager";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (!transformedName.equals(SERVERCONFIGURATIONMANAGER_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformServerConfigurationManager(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform ServerConfigurationManager.", e);
		}
	}

	private byte[] transformServerConfigurationManager(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);

		String targetMethodName = "func_72366_a";// createPlayerForUser
		String targetMethodDesc = "(Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;";//"(Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;";
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			MethodInsnNode targetMethodInsnNode = null;
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {//for (AbstractInsnNode aiNode : ) {
				AbstractInsnNode aiNode = insnList[i];

				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (("net/minecraft/entity/player/EntityPlayerMP").equals(this.map(tiNode.desc)) && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && ("net/minecraft/entity/player/EntityPlayerMP").equals(this.map(miNode.owner)) && miNode.name.equals("<init>") && ("(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V").equals(this.mapMethodDesc(miNode.desc))) {
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
					}
				}
			}
		}

		targetMethodName = "func_72368_a";// respawnPlayer
		targetMethodDesc = "(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";//"(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";
		targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {//for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				AbstractInsnNode aiNode = insnList[i];
				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (("net/minecraft/entity/player/EntityPlayerMP").equals(this.map(tiNode.desc)) && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (("<init>").equals(miNode.name) && ("net/minecraft/entity/player/EntityPlayerMP").equals(this.map(miNode.owner)) && miNode.getOpcode() == INVOKESPECIAL && ("(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V").equals(this.mapMethodDesc(miNode.desc))) {
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
					}
				}
			}
		}

		bytes = this.decode(cNode);

		return bytes;
	}

}
