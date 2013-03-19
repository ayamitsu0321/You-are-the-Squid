package ayamitsu.urtsquid.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IClassTransformer;

/**
 * transform 1.62 to 0.425
 */
public class Transformer162Ldc extends TransformerBase {

	private static final String ENTITYFISHFOOK_CLASS_NAME = "net.minecraft.entity.projectile.EntityFishFook";// EntityFishFook
	private static final String ITEM_CLASS_NAME = "net.minecraft.item.Item";// Item
	private static final String ITEMBOAT_CLASS_NAME = "net.minecraft.item.ItemBoat";// ItemBoat
	private static final String ITEMBUCKET_CLASS_NAME = "net.minecraft.item.ItemBucket";// ItemBucket
	private static final String ITEMENDEREYE_CLASS_NAME = "net.minecraft.item.ItemEnderEye";// ItemEnderEye
	private static final String NETSERVERHANDLER_CLASS_NAME = "net.minecraft.network.NetServerHandler";// NetServerHandler

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (transformedName.equals(ENTITYFISHFOOK_CLASS_NAME)) {
			return this.transformEntityFishHook(bytes);
		} else if (transformedName.equals(ITEM_CLASS_NAME)) {
			return this.transformItem(bytes);
		} else if (transformedName.equals(ITEMBOAT_CLASS_NAME)) {
			return this.transformItemBoat(bytes);
		} else if (transformedName.equals(ITEMBUCKET_CLASS_NAME)) {
			return this.transformItemBucket(bytes);
		} else if (transformedName.equals(ITEMENDEREYE_CLASS_NAME)) {
			return this.transformItemEnderEye(bytes);
		} else if (transformedName.equals(NETSERVERHANDLER_CLASS_NAME)) {
			return this.transformNetServerHandler(bytes);
		}

		return bytes;
	}

	private byte[] transformEntityFishHook(byte[] bytes) {
		ASMDebugUtils.info("Found EntityFishFook");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// <init> (LWorld;LEntityPlayer;)V
			if ("<init>".equals(mNode.name) && "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V".equals(mNode.desc)) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.62D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.425D)));
							ASMDebugUtils.info("Override EntityFishHook");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

	private byte[] transformItem(byte[] bytes) {
		ASMDebugUtils.info("Found Item");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// getMovingObjectPositionFromPlayer (LWorld;LEntityPlayer;Z)LMovingObjectPosition;
			if ("func_77621_a".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Z)Lnet/minecraft/util/MovingObjectPosition;".equals(this.mapMethodDesc(mNode.desc))) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.62D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.425D)));
							ASMDebugUtils.info("Override Item");
							break;
						} else if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425D)));
							ASMDebugUtils.info("Override Item");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

	private byte[] transformItemBoat(byte[] bytes) {
		ASMDebugUtils.info("Found ItemBoat");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// onItemRightClick (Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;
			if ("func_77659_a".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;".equals(this.mapMethodDesc(mNode.desc))) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.62D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.425D)));
							ASMDebugUtils.info("Override ItemBoat");
							break;
						} else if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425D)));
							ASMDebugUtils.info("Override ItemBoat");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

	private byte[] transformItemBucket(byte[] bytes) {
		ASMDebugUtils.info("Found ItemBucket");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// onItemRightClick (Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;
			if ("func_77659_a".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;".equals(this.mapMethodDesc(mNode.desc))) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.62D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.425D)));
							ASMDebugUtils.info("Override ItemBucket");
							break;
						} else if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425D)));
							ASMDebugUtils.info("Override ItemBucket");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

	private byte[] transformItemEnderEye(byte[] bytes) {
		ASMDebugUtils.info("Found ItemEnderEye");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// onItemRightClick (Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;
			if ("func_77659_a".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;".equals(this.mapMethodDesc(mNode.desc))) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.62D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.425D)));
							ASMDebugUtils.info("Override ItemEnderEye");
							break;
						} else if (liNode.cst instanceof Float && ((Float)liNode.cst).floatValue() == 1.62F) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Float(0.425D)));
							ASMDebugUtils.info("Override ItemEnderEye");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

	private byte[] transformNetServerHandler(byte[] bytes) {
		ASMDebugUtils.info("Found NetServerHandler");
		ClassNode cNode = this.encode(bytes);

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			// setPlayerLocation (DDDFF)V
			if ("func_72569_a".equals(this.mapMethodName(cNode.name, mNode.name, mNode.desc)) && "(DDDFF)V".equals(mNode.desc)) {
				AbstractInsnNode[] insnList = mNode.instructions.toArray();

				for (int i = 0; i < insnList.length; i++) {
					if (insnList[i] instanceof LdcInsnNode) {
						LdcInsnNode liNode = (LdcInsnNode)insnList[i];

						if (liNode.cst instanceof Double && ((Double)liNode.cst).doubleValue() == 1.6200000047683716D) {
							mNode.instructions.set(liNode, new LdcInsnNode(new Double(0.4260000047683716D)));
							ASMDebugUtils.info("Override NetServerHandler");
							break;
						}
					}
				}
			}
		}

		return this.decode(cNode);
	}

}
