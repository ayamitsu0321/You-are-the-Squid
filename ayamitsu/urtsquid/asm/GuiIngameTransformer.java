package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.relauncher.IClassTransformer;

public class GuiIngameTransformer implements IClassTransformer, Opcodes {

	// for 1.4.7
	private static final String GUIINGAME_CLASS_NAME = "atr";//"GuiIngamemenu";

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!name.equals(GUIINGAME_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformGuiIngame(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load GuiIngameTransformer.", e);
		}
	}

	private byte[] transformGuiIngame(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);

		String targetMethodName = "a";// renderGameOverlay
		String targetMethodDesc = "(FZII)V";// void (float, boolean, int, int)
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			boolean flag = false;
			String var_Name = "a";// isInsideMaterial
			String var_Desc = "(Lagi;)Z";// boolean (Material)
			String var_Owner = "ays";// EntityClientPlayerMP
			int var_Opecode = INVOKEVIRTUAL;

			for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.name.equals(var_Name) && miNode.desc.equals(var_Desc) && miNode.owner.equals(var_Owner) && miNode.getOpcode() == var_Opecode) {
						flag = true;
					}
				}

				if (aiNode instanceof JumpInsnNode) {
					JumpInsnNode jiNode = (JumpInsnNode)aiNode;

					if (flag && jiNode.getOpcode() == IFEQ && jiNode.getType() == 7) {
						jiNode.setOpcode(IFNE);
						flag = false;
						System.out.println("[URTSquid]Override Operation");
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
