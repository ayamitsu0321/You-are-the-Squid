package ayamitsu.urtsquid.asm;

import java.util.logging.Logger;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class ASMDebugLogger {

	public static final Logger logger = Logger.getLogger("ASMDebug");

	public static void info(String info) {
		//logger.info(info);
		System.out.println("[ASMDebug]" + info);
	}

	public static void log(ClassNode cNode) {
		log(cNode, cNode.name, cNode.signature);
	}

	public static void log(MethodNode mNode) {
		log(mNode, mNode.name, mNode.desc, mNode.signature);
	}

	public static void log(FieldNode fNode) {
		log(fNode, fNode.name, fNode.desc, fNode.signature);
	}

	public static void log(LocalVariableNode lvNode) {
		log(lvNode, lvNode.name, lvNode.desc, lvNode.signature);
	}

	public static void log(InsnList insnList) {
		for (AbstractInsnNode aiNode : insnList.toArray()) {
			log(aiNode);
		}
	}

	public static void log(AbstractInsnNode aiNode) {
		if (aiNode instanceof FieldInsnNode) {
			FieldInsnNode fiNode = (FieldInsnNode)aiNode;
			log(fiNode, fiNode.name, fiNode.desc, fiNode.owner, fiNode.getOpcode());
		} else if (aiNode instanceof FrameNode) {
			FrameNode fNode = (FrameNode)aiNode;
			log(fNode, fNode.getOpcode());
		} else if (aiNode instanceof IincInsnNode) {
			IincInsnNode iiNode = (IincInsnNode)aiNode;
			log(iiNode, iiNode.getOpcode());
		} else if (aiNode instanceof InsnNode) {
			InsnNode iNode = (InsnNode)aiNode;
			log(iNode, iNode.getOpcode());
		} else if (aiNode instanceof IntInsnNode) {
			IntInsnNode iiNode = (IntInsnNode)aiNode;
			log(iiNode, iiNode.getOpcode());
		} else if (aiNode instanceof InvokeDynamicInsnNode) {
			InvokeDynamicInsnNode idiNode = (InvokeDynamicInsnNode)aiNode;
			log(idiNode, idiNode.name, idiNode.desc, idiNode.getOpcode());
		} else if (aiNode instanceof JumpInsnNode) {
			JumpInsnNode jiNode = (JumpInsnNode)aiNode;
			log(jiNode, jiNode.getOpcode());
		} else if (aiNode instanceof LabelNode) {
			LabelNode lNode = (LabelNode)aiNode;
			log(lNode, lNode.getOpcode());
		} else if (aiNode instanceof LdcInsnNode) {
			LdcInsnNode liNode = (LdcInsnNode)aiNode;
			log(liNode, liNode.getOpcode());
		} else if (aiNode instanceof LineNumberNode) {
			LineNumberNode lnNode = (LineNumberNode)aiNode;
			log(lnNode, lnNode.getOpcode());
		} else if (aiNode instanceof LookupSwitchInsnNode) {
			LookupSwitchInsnNode lsiNode = (LookupSwitchInsnNode)aiNode;
			log(lsiNode, lsiNode.getOpcode());
		} else if (aiNode instanceof MethodInsnNode) {
			MethodInsnNode miNode = (MethodInsnNode)aiNode;
			log(miNode, miNode.name, miNode.owner, miNode.desc, miNode.getOpcode());
		} else if (aiNode instanceof MultiANewArrayInsnNode) {
			MultiANewArrayInsnNode manaiNode = (MultiANewArrayInsnNode)aiNode;
			log(manaiNode, manaiNode.desc, manaiNode.getOpcode());
		} else if (aiNode instanceof TableSwitchInsnNode) {
			TableSwitchInsnNode tsiNode = (TableSwitchInsnNode)aiNode;
			log(tsiNode, tsiNode.getOpcode());
		} else if (aiNode instanceof TypeInsnNode) {
			TypeInsnNode tiNode = (TypeInsnNode)aiNode;
			log(tiNode, tiNode.desc, tiNode.getOpcode());
		} else if (aiNode instanceof VarInsnNode) {
			VarInsnNode viNode = (VarInsnNode)aiNode;
			log(viNode, viNode.var, viNode.getOpcode());
		}
	}

	private static void log(Object instance, int opecode) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendOpecode(opecode).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendOpecode(opecode).trim().toString());
	}

	private static void log(Object instance, int var, int opecode) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendVar(var).appendOpecode(opecode).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendVar(var).appendOpecode(opecode).trim().toString());
	}

	private static void log(Object instance, String desc, int opecode) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendDesc(desc).appendOpecode(opecode).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendDesc(desc).appendOpecode(opecode).trim().toString());
	}

	private static void log(Object instance, String name, String desc, int opecode) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendName(name).appendDesc(desc).appendOpecode(opecode).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendName(name).appendDesc(desc).appendOpecode(opecode).trim().toString());
	}

	private static void log(Object instance, String name, String owner, String desc, int opecode) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendName(name).appendOwner(owner).appendDesc(desc).appendOpecode(opecode).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendName(name).appendOwner(owner).appendDesc(desc).appendOpecode(opecode).trim().toString());
	}

	private static void log(Object instance, String name, String signature) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendName(name).appendSignature(signature).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendName(name).appendSignature(signature).trim().toString());
	}

	private static void log(Object instance, String name, String desc, String signature) {
		//logger.info((new DebugStringBuilder()).appendClass(instance).appendName(name).appendDesc(desc).appendSignature(signature).trim().toString());
		System.out.println("[ASMDebug]" + (new DebugStringBuilder()).appendClass(instance).appendName(name).appendDesc(desc).appendSignature(signature).trim().toString());
	}

	private static class DebugStringBuilder {

		private String instance = "";

		public DebugStringBuilder() {}

		public DebugStringBuilder append(Object obj) {
			this.instance += String.valueOf(obj);
			return this;
		}

		public DebugStringBuilder appendClass(Object instance) {
			this.instance += (instance == null ? "null" : instance.getClass().getSimpleName()) + ", ";
			return this;
		}

		public DebugStringBuilder appendName(String name) {
			this.instance += "name:" + name + ", ";
			return this;
		}

		public DebugStringBuilder appendOwner(String owner) {
			this.instance += "owner:" + owner + ", ";
			return this;
		}

		public DebugStringBuilder appendDesc(String desc) {
			this.instance += "desc:" + desc + ", ";
			return this;
		}

		public DebugStringBuilder appendOpecode(int opecode) {
			this.instance += "opecode:" + Integer.toHexString(opecode) + ", ";
			return this;
		}

		public DebugStringBuilder appendVar(int var) {
			this.instance += "var:" + Integer.toString(var) + ", ";
			return this;
		}

		public DebugStringBuilder appendSignature(String signature) {
			this.instance += "signature:" + signature + ", ";
			return this;
		}

		public DebugStringBuilder trim() {
			this.instance = this.instance.trim();

			if (this.instance.endsWith(",")) {
				this.instance = this.instance.substring(0, this.instance.length() - 1);
			}

			return this;
		}

		public String toString() {
			return this.instance;
		}

	}

}
