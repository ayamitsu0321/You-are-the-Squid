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

	// for 1.4.7
	private static final String SERVERCONFIGURATIONMANAGER_CLASS_NAME = "gm";//"ServerConfigurationManager";

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!name.equals(SERVERCONFIGURATIONMANAGER_CLASS_NAME)) {
			return bytes;
		}

		ASMDebugUtils.info("Found ServerConfigurationManager");

		try {
			return this.transformServerConfigurationManager(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform ServerConfigurationManager.", e);
		}
	}

	private byte[] transformServerConfigurationManager(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);

		String targetMethodName = "a";// createPlayerForUser
		String targetMethodDesc = "(Ljava/lang/String;)Liq;";//"(Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;";
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {

			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				ASMDebugUtils.info("found createPlayerForUser");
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

					if (tiNode.desc.equals("iq") && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						ASMDebugUtils.info("Override TypeInsnNode iq to EntityPlayerSquidMP");
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && miNode.owner.equals("iq") && miNode.name.equals("<init>") && miNode.desc.equals("(Lnet/minecraft/server/MinecraftServer;Lyc;Ljava/lang/String;Lir;)V")) {
						//miNode.owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						//insnList[i] = new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc));
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
						ASMDebugUtils.info("Override ServerConfigurationManager createPlayerForUser");
					}
				}
			}
		}

		targetMethodName = "a";// respawnPlayer
		targetMethodDesc = "(Liq;IZ)Liq;";//"(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";
		targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				ASMDebugUtils.info("found respawnPlayer");
				break;
			}
		}

		if (targetMethodNode != null) {
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {//for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				AbstractInsnNode aiNode = insnList[i];
				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (tiNode.desc.equals("iq") && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						ASMDebugUtils.info("Override TypeInsnNode iq to EntityPlayerSquidMP");
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.name.equals("<init>") && miNode.owner.equals("iq") && miNode.getOpcode() == INVOKESPECIAL && miNode.desc.equals("(Lnet/minecraft/server/MinecraftServer;Lyc;Ljava/lang/String;Lir;)V")) {
						//miNode.owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						//insnList[i] = new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc));
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
						ASMDebugUtils.info("Override ServerConfigurationManager respawnPlayer");
					}
				}
			}
		}

		bytes = this.decode(cNode);

		return bytes;
	}

}
