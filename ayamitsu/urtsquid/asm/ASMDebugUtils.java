package ayamitsu.urtsquid.asm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

import org.objectweb.asm.Opcodes;
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

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLInjectionData;

public final class ASMDebugUtils implements Opcodes {

	private static final ASMDebugUtils SINGLETON = new ASMDebugUtils();
	private static final Logger logger = Logger.getLogger("ASMDebug");

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void log(String msg) {
		logger.fine(msg);
	}

	public static void logAll(ClassNode cNode) {
		logger.fine("start log");
		log(cNode);
		logger.fine("field node log start");

		for (FieldNode fNode : (List<FieldNode>)cNode.fields) {
			log(fNode);
		}

		logger.fine("field node log end");
		logger.fine("method node log start");

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			logAll(mNode);
		}

		logger.fine("method node log end");
		logger.fine("end log");
	}

	public static void logAll(MethodNode mNode) {
		log(mNode);
		logger.fine("method instruction node log start");

		for (AbstractInsnNode aiNode : mNode.instructions.toArray()) {
			log(aiNode);
		}

		logger.fine("method instruction node log end");
		logger.fine("method local variable node log start");

		for (LocalVariableNode lvNode : (List<LocalVariableNode>)mNode.localVariables) {
			log(lvNode);
		}

		logger.fine("method local variable node log end");
	}

	public static void log(ClassNode cNode) {
		logger.fine((new DebugStringBuilder().appendClass(cNode).appendName(cNode.name).appendSignature(cNode.signature).appendVersion(cNode.version)).toString());
	}

	public static void log(FieldNode fNode) {
		logger.fine((new DebugStringBuilder().appendClass(fNode).appendName(fNode.name).appendDesc(fNode.desc).appendSignature(fNode.signature)).toString());
	}

	public static void log(MethodNode mNode) {
		logger.fine((new DebugStringBuilder().appendClass(mNode).appendName(mNode.name).appendDesc(mNode.desc).appendSignature(mNode.signature)).toString());
	}

	public static void log(LocalVariableNode lvNode) {
		logger.fine((new DebugStringBuilder().appendClass(lvNode).appendName(lvNode.name).appendDesc(lvNode.desc).appendSignature(lvNode.signature).appendIndex(lvNode.index)).toString());
	}

	public static void log(InsnList insnList) {
		for (AbstractInsnNode aiNode : insnList.toArray()) {
			log(aiNode);
		}
	}

	public static void log(AbstractInsnNode aiNode) {
		if (aiNode instanceof FieldInsnNode) {
			FieldInsnNode fiNode = (FieldInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(fiNode).appendOpcode(fiNode.getOpcode()).appendName(fiNode.name).appendOwner(fiNode.owner).appendDesc(fiNode.desc)).toString());
		} else if (aiNode instanceof FrameNode) {
			FrameNode fNode = (FrameNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(fNode).appendOpcode(fNode.getOpcode())).toString());
		} else if (aiNode instanceof IincInsnNode) {
			IincInsnNode iiNode = (IincInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(iiNode).appendOpcode(iiNode.getOpcode()).appendVar(iiNode.var)).toString());
		} else if (aiNode instanceof InsnNode) {
			InsnNode iNode = (InsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(iNode).appendOpcode(iNode.getOpcode())).toString());
		} else if (aiNode instanceof IntInsnNode) {
			IntInsnNode iiNode = (IntInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(iiNode).appendOpcode(iiNode.getOpcode())).toString());
		} else if (aiNode instanceof InvokeDynamicInsnNode) {
			InvokeDynamicInsnNode idiNode = (InvokeDynamicInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(idiNode).appendOpcode(idiNode.getOpcode()).appendName(idiNode.name).appendDesc(idiNode.desc)).toString());
		} else if (aiNode instanceof JumpInsnNode) {
			JumpInsnNode jiNode = (JumpInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(jiNode)).appendOpcode(jiNode.getOpcode()).toString());
		} else if (aiNode instanceof LabelNode) {
			LabelNode lNode = (LabelNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(lNode).appendOpcode(lNode.getOpcode())).toString());
		} else if (aiNode instanceof LdcInsnNode) {
			LdcInsnNode liNode = (LdcInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(liNode).appendOpcode(liNode.getOpcode())).toString());
		} else if (aiNode instanceof LineNumberNode) {
			LineNumberNode lnNode = (LineNumberNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(lnNode).appendOpcode(lnNode.getOpcode())).toString());
		} else if (aiNode instanceof LookupSwitchInsnNode) {
			LookupSwitchInsnNode lsiNode = (LookupSwitchInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(lsiNode).appendOpcode(lsiNode.getOpcode())).toString());
		} else if (aiNode instanceof MethodInsnNode) {
			MethodInsnNode miNode = (MethodInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(miNode).appendOpcode(miNode.getOpcode()).appendName(miNode.name).appendOwner(miNode.owner).appendDesc(miNode.desc)).toString());
		} else if (aiNode instanceof MultiANewArrayInsnNode) {
			MultiANewArrayInsnNode manaiNode = (MultiANewArrayInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(manaiNode).appendOpcode(manaiNode.getOpcode()).appendDesc(manaiNode.desc)).toString());
		} else if (aiNode instanceof TableSwitchInsnNode) {
			TableSwitchInsnNode tsiNode = (TableSwitchInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(tsiNode).appendOpcode(tsiNode.getOpcode())).toString());
		} else if (aiNode instanceof TypeInsnNode) {
			TypeInsnNode tiNode = (TypeInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(tiNode).appendOpcode(tiNode.getOpcode()).appendDesc(tiNode.desc)).toString());
		} else if (aiNode instanceof VarInsnNode) {
			VarInsnNode viNode = (VarInsnNode)aiNode;
			logger.fine((new DebugStringBuilder().appendClass(viNode).appendOpcode(viNode.getOpcode()).appendVar(viNode.var)).toString());
		}
	}

	public static String translateOpcode(int opcode) {
		try {
			Field[] fields = Opcodes.class.getDeclaredFields();// from 57

			for (int i = 57; i < fields.length; i++) {
				if (fields[i].getInt(SINGLETON) == opcode) {
					return fields[i].getName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "unknown";
	}

	public static void exportToFile(byte[] bytes, String path) throws IOException {
		File currentDir = (File)FMLInjectionData.data()[6];
		File saveFile = new File(currentDir, path);

		if (!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
			throw new IOException("cannot create dir:" + saveFile.getParent());
		}

		if (!saveFile.exists() && !saveFile.createNewFile() || !saveFile.canWrite()) {
			throw new IOException("cannot create or write file:" + saveFile.getPath());
		}

		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
		dos.write(bytes);
		dos.close();
	}

	static {
		logger.setParent(FMLLog.getLogger());
	}

	private static class DebugStringBuilder {

		private String instance = "";

		public DebugStringBuilder() {}

		public DebugStringBuilder append(Object obj) {
			this.instance += String.valueOf(obj);
			return this;
		}

		public DebugStringBuilder appendVersion(int version) {
			this.instance += "version:" + Integer.toString(version) + ", ";
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

		public DebugStringBuilder appendOpcode(int opcode) {
			this.instance += "opcode:" + Integer.toHexString(opcode) + "(" + ASMDebugUtils.translateOpcode(opcode) + ")" + ", ";
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

		public DebugStringBuilder appendIndex(int index) {
			this.instance += "index:" + Integer.toString(index) + ", ";
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
