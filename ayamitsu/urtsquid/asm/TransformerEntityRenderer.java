package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.relauncher.FMLRelauncher;

public class TransformerEntityRenderer extends TransformerBase {

	private static final String ENTITYRENDERER_CLASS_NAME = "net.minecraft.client.renderer.EntityRenderer";// EntityRenderer

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (!FMLRelauncher.side().equals("CLIENT") || !transformedName.equals(ENTITYRENDERER_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformEntityRenderer(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform EntityRenderer", e);
		}
	}

	private byte[] transformEntityRenderer(byte[] arrayOfByte) {
		ASMDebugUtils.info("Found EntityRenderer");
		ClassNode cNode = this.encode(arrayOfByte);

		boolean addedTransratef = false;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// orientCamera (F)V
			if ("func_78467_g".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(F)V".equals(mNode.desc)) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)aiNode;

						if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425F)));
							ASMDebugUtils.info("Override orientCamera(1)");
							continue;
						}
					}

					if (!addedTransratef && aiNode instanceof MethodInsnNode) {
						MethodInsnNode miNode = (MethodInsnNode)aiNode;

						if (miNode.getOpcode() == INVOKESTATIC &&miNode.owner.equals("org/lwjgl/opengl/GL11") && miNode.name.equals("glTranslatef") && miNode.desc.equals("(FFF)V")) {
							InsnList overrideList = new InsnList();
							overrideList.add(new InsnNode(FCONST_0));
							overrideList.add(new LdcInsnNode(new Float(-1.195)));
							overrideList.add(new InsnNode(FCONST_0));
							overrideList.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glTranslatef", "(FFF)V"));
							mNode.instructions.insert(miNode, overrideList);
							ASMDebugUtils.info("Override orientCamera(2)");
							addedTransratef = true;
						}
					}
				}

				break;
			}
		}

		return this.decode(cNode);
	}

}
