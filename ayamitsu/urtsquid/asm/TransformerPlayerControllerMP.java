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

public class TransformerPlayerControllerMP implements IClassTransformer, Opcodes {

	// for 1.4.7
	private static final String PLAYERCONTROLLERMP_CLASS = "ayo";// PlayerControllerMP

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!FMLRelauncher.side().equals("CLIENT") || !name.equals(PLAYERCONTROLLERMP_CLASS)) {
			return bytes;
		}

		ASMDebugUtils.info("Found PlayerControllerMP");

		try {
			return transformPlayerControllerMP(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform PlayerConrollerMP", e);
		}
	}

	private byte[] transformPlayerControllerMP(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);

		String targetMethodName = "a";// func_78754_a
		String targetMethodDesc = "(Lyc;)Lays;";// EntityClientPlayerMP (World)
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (tiNode.getOpcode() == NEW && tiNode.desc.equals("ays")) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidSP";
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && miNode.name.equals("<init>") && miNode.owner.equals("ays") && miNode.desc.equals("(Lnet/minecraft/client/Minecraft;Lyc;Lata;Layh;)V")) {
						miNode.owner = "ayamitsu/urtsquid/player/EntityPlayerSquidSP";
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
