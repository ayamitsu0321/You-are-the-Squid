package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ayamitsu.util.reflect.Reflector;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;

public class TransformerGuiIngame extends TransformerBase {

	private static final String GUIINGAME_CLASS_NAME = "net.minecraft.client.gui.GuiIngame";//"GuiIngame";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (FMLLaunchHandler.side() != Side.CLIENT || !transformedName.equals(GUIINGAME_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformGuiIngame(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform GuiIngame.", e);
		}
	}

	private byte[] transformGuiIngame(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);

		String targetMethodName = Reflector.isRenameTable() ? "renderGameOverlay" : "func_73830_a";// renderGameOverlay
		String targetMethodDesc = "(FZII)V";// void (float, boolean, int, int)
		MethodNode targetMethodNode = null;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				break;
			}
		}

		if (targetMethodNode != null) {
			boolean flag = false;
			String var_Name = "func_70055_a";// isInsideOfMaterial
			String var_Desc = "(Lnet/minecraft/block/material/Material;)Z";// boolean (Material)
			String var_Owner = "net/minecraft/client/entity/EntityClientPlayerMP";// EntityClientPlayerMP
			int var_Opecode = INVOKEVIRTUAL;

			for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (var_Name.equals(this.mapMethodName(miNode.owner, miNode.name, miNode.desc)) && var_Desc.equals(this.mapMethodDesc(miNode.desc)) && var_Owner.equals(this.map(miNode.owner)) && miNode.getOpcode() == var_Opecode) {
						flag = true;
					}
				}

				if (aiNode instanceof JumpInsnNode) {
					JumpInsnNode jiNode = (JumpInsnNode)aiNode;

					if (flag && jiNode.getOpcode() == IFEQ && jiNode.getType() == 7) {
						jiNode.setOpcode(IFNE);// flag -> !flag
						flag = false;
						break;
					}
				}
			}

			bytes = this.decode(cNode);
		}

		return bytes;
	}
}
