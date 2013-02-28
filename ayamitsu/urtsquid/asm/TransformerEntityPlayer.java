package ayamitsu.urtsquid.asm;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformerEntityPlayer extends TransformerBase {

	// for 1.4.7
	private static final String ENTITYPLAYER_CLASS_NAME = "qx";// EntityPlayer

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!name.equals(ENTITYPLAYER_CLASS_NAME)) {
			return bytes;
		}

		ASMDebugUtils.info("Found EntityPlayer.");

		try {
			return this.transformEntityPlayer(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform EntityPlayer", e);
		}
	}

	private byte[] transformEntityPlayer(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);
		String targetMethodName = "a";// sleepInBedAt
		String targetMethodDesc = "(III)Lqy;";// (int, int, int)LEnumStatus;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				//ASMDebugUtils.logAll(mNode);
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)aiNode;

						if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 0.9375F) {
							AbstractInsnNode aiNode1 = liNode.getNext();

							if (aiNode1 instanceof InsnNode && aiNode1.getOpcode() == FADD) {
								mNode.instructions.set(aiNode1, new InsnNode(FSUB));
								ASMDebugUtils.info("Override EntityPlayer + 0.9375 to - 0.9375");
							}
						}
					}
				}
			}
		}

		bytes = this.decode(cNode);
		return bytes;
	}

}
