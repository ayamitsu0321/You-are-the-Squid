package ayamitsu.urtsquid.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class TransformerBlockBed extends TransformerBase {

	private static final String BLOCKBED_CLASS_NAME = "net.minecraft.block.BlockBed";// BlockBed

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (!transformedName.equals(BLOCKBED_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.trabsformBlockBed(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform BlockBed", e);
		}
	}

	private byte[] trabsformBlockBed(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);

		String targetMethodName = "func_72226_b";// getNearestEmptyChunkCoordinates
		String targetMethodDesc = "(Lnet/minecraft/world/World;IIII)Lnet/minecraft/util/ChunkCoordinates;";// ChunkCoordinates(World, int, int, int, int)

		for (MethodNode mNode : cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();
				boolean replaced = false;

				for (int i = 0; i < insnList.length; i++) {
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof MethodInsnNode) {
						MethodInsnNode miNode = (MethodInsnNode)aiNode;

						// world.isAirBlock(x, y, z)
						if (miNode.getOpcode() == INVOKEVIRTUAL && ("net/minecraft/world/World").equals(this.map(miNode.owner)) && ("func_72799_c").equals(this.mapMethodName(miNode.owner, miNode.name, miNode.desc)) && ("(III)Z").equals(miNode.desc)) {

							if (!replaced) {
								replaced = true;
								MethodInsnNode overrideMiNode = new MethodInsnNode(INVOKEVIRTUAL, miNode.owner, this.unmapMethodName(miNode.owner, "func_72803_f"), "(III)L" + this.unmap("net/minecraft/block/material/Material") + ";");
								// replace
								mNode.instructions.set(miNode, overrideMiNode);
								//mNode.instructions.set(overrideMiNode.getNext(), new JumpInsnNode(IFNE, null));
								((JumpInsnNode)overrideMiNode.getNext()).setOpcode(IFNE);
								// material.isSolid()
								String materialClass = "net/minecraft/block/material/Material";
								mNode.instructions.insert(overrideMiNode, new MethodInsnNode(INVOKEVIRTUAL, this.unmap(materialClass), this.unmapMethodName(materialClass, "func_76220_a"), "()Z"));
							} else {
								mNode.instructions.remove(miNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
								mNode.instructions.remove(miNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
								mNode.instructions.remove(miNode.getPrevious().getPrevious().getPrevious().getPrevious());
								mNode.instructions.remove(miNode.getPrevious().getPrevious().getPrevious());
								mNode.instructions.remove(miNode.getPrevious().getPrevious());
								mNode.instructions.remove(miNode.getPrevious());
								mNode.instructions.remove(miNode.getNext());
								mNode.instructions.remove(miNode);
							}
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

}
