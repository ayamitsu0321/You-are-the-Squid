package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerEntityRenderer implements IClassTransformer, Opcodes {

	// for 1.4.7
	private static final String ENTITYRENDERER_CLASS_NAME = "ban";// EntityRenderer

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!FMLRelauncher.side().equals("CLIENT") || !name.equals(ENTITYRENDERER_CLASS_NAME)) {
			return bytes;
		}

		ASMDebugUtils.info("Found EntityRenderer");

		try {
			return this.transfromEntityRenderer(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform EntityRenderer.", e);
		}
	}

	private byte[] transfromEntityRenderer(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);

		String targetMethodName = "g";
		String targetMethodDesc = "(F)V";
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
			}
		}

		if (targetMethodNode != null) {
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {
				AbstractInsnNode aiNode = insnList[i];

				if (aiNode instanceof LdcInsnNode) {
					LdcInsnNode liNode = (LdcInsnNode)aiNode;

					if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
						targetMethodNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425F)));
						break;
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
