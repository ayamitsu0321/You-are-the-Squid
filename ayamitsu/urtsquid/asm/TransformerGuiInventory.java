package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;

public class TransformerGuiInventory extends TransformerBase {

	private static final String GUIINVENTORY_CLASS_NAME = "net.minecraft.client.gui.inventory.GuiInventory";// GuiInventory

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (FMLLaunchHandler.side() != Side.CLIENT || !transformedName.equals(GUIINVENTORY_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformGuiInventory(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform GuiInventory", e);
		}
	}

	private byte[] transformGuiInventory(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);
		String targetMethodName = "func_110423_a";// func_110423_a
		String targetMethodDesc = "(IIIFFLnet/minecraft/entity/EntityLivingBase;)V";// (IIIFFLnet/minecraft/entity/EntityLivingBase;)V

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {
				MethodInsnNode targetMethodInsnNode = null;
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof MethodInsnNode) {
						MethodInsnNode miNode = (MethodInsnNode)aiNode;

						if (miNode.name.equals("glTranslatef")) {
							targetMethodInsnNode = miNode;
						}
					}
				}

				if (targetMethodInsnNode != null) {
					InsnList insns = new InsnList();
					insns.add(new InsnNode(FCONST_0));
					insns.add(new LdcInsnNode(new Float(1.2F)));
					insns.add(new InsnNode(FCONST_0));
					insns.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glTranslatef", "(FFF)V"));
					mNode.instructions.insertBefore(targetMethodInsnNode, insns);
					break;
				}
			}
		}

		bytes = this.decode(cNode);
		return bytes;
	}

}
