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

import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransfomerNetClientHandler implements IClassTransformer, Opcodes {

	private static final String NETCLIENTHANDLER_CLASS_NAME = "ayh";// NetClientHandler

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!FMLRelauncher.side().equals("CLIENT") || !name.equals(NETCLIENTHANDLER_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformNetClientHandler(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform NetClientHandler", e);
		}
	}

	private byte[] transformNetClientHandler(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);

		String targetMethodName = "a";// handleLogin
		String targetMethodDesc = "(Ldw;)V";// void (PacketLogin)
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
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

					if (tiNode.getOpcode() == NEW && tiNode.desc.equals("ayo")) {
						tiNode.desc = "ayamitsu/urtsquid/player/PlayerControllerSquid";
						ASMDebugUtils.info("Override TypeInsnNode ayo to PlayerControllerSquid");
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && miNode.name.equals("<init>") && miNode.owner.equals("ayo") && miNode.desc.equals("(Lnet/minecraft/client/Minecraft;Layh;)V")) {
						//miNode.owner = "ayamitsu/urtsquid/player/PlayerControllerSquid";
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/PlayerControllerSquid", new String(miNode.name), new String(miNode.desc)));
						ASMDebugUtils.info("Override MethodInsnNode ayo to PlayerControllerSquid");
					}
				}
			}

			ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			cNode.accept(cWriter);
			bytes = cWriter.toByteArray();
		}

		return bytes;
	}

}
