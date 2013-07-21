package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformerEntityPlayer extends TransformerBase {

	private static String ENTITYPLAYER_CLASS_NAME = "net.minecraft.entity.player.EntityPlayer";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (!transformedName.equals(ENTITYPLAYER_CLASS_NAME)) {
			return bytes;
		}

		try {
			return this.transformEntityPlayer(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform EntityPlayer", e);
		}
	}

	private byte[] transformEntityPlayer(byte[] bytes) {
		ClassNode cNode = this.encode(bytes);
		String targetMethodName = "func_71056_a";// verifyRespawnCoordinates
		String targetMethodDesc = "(Lnet/minecraft/world/World;Lnet/minecraft/util/ChunkCoordinates;Z)Lnet/minecraft/util/ChunkCoordinates;";// (Lnet/minecraft/world/World;Lnet/minecraft/util/ChunkCoordinates;Z)Lnet/minecraft/util/ChunkCoordinates;

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			if (targetMethodName.equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && targetMethodDesc.equals(this.mapMethodDesc(mNode.desc))) {

				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					AbstractInsnNode aiNode = insnList[i];

					if (aiNode instanceof MethodInsnNode) {
						MethodInsnNode miNode = (MethodInsnNode)aiNode;

						if (miNode.getOpcode() == INVOKEVIRTUAL && ("net/minecraft/block/material/Material").equals(this.map(miNode.owner)) && ("func_76220_a").equals(this.mapMethodName(miNode.owner, miNode.name, miNode.desc)) && ("()Z").equals(this.mapMethodDesc(miNode.desc))) {
							mNode.instructions.remove(miNode.getPrevious());
							mNode.instructions.remove(miNode.getNext());
							mNode.instructions.remove(miNode);
						}
					}

					if (aiNode instanceof VarInsnNode) {
						VarInsnNode viNode = (VarInsnNode)aiNode;

						if (viNode.getOpcode() == ILOAD && viNode.var == 9) {
							mNode.instructions.remove(viNode.getNext());
							mNode.instructions.remove(viNode);
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

}
