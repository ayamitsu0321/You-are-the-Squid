package ayamitsu.urtsquid.asm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerServerConfigurationManager implements IClassTransformer, Opcodes {

	// for 1.4.7
	private static final String SERVERCONFIGURATIONMANAGER_CLASS_NAME = "gm";//"ServerConfigurationManager";

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!name.equals(SERVERCONFIGURATIONMANAGER_CLASS_NAME)) {
			return bytes;
		}

		System.out.println("Found ServerConfigurationManager");

		try {
			return this.transformServerConfigurationManager(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Failed to transform ServerConfigurationManager.", e);
		}
	}

	private byte[] transformServerConfigurationManager(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);

		String targetMethodName = "a";// createPlayerForUser
		String targetMethodDesc = "(Ljava/lang/String;)Liq;";//"(Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;";
		MethodNode targetMethodNode = null;

		ASMDebugUtils.log("MethodNode start");

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			ASMDebugUtils.log(mNode);

			if (targetMethodNode == null && targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				ASMDebugUtils.log("found createPlayerForUser");
				//break;
			}
		}

		ASMDebugUtils.log("MethodNode end");

		if (targetMethodNode != null) {
			ASMDebugUtils.log("AbstractInsnNode start");

			MethodInsnNode targetMethodInsnNode = null;
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {//for (AbstractInsnNode aiNode : ) {
				AbstractInsnNode aiNode = insnList[i];
				ASMDebugUtils.log(aiNode);

				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (tiNode.desc.equals("iq") && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						System.out.println("Override TypeInsnNode iq to EntityPlayerSquidMP");
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.getOpcode() == INVOKESPECIAL && miNode.owner.equals("iq") && miNode.name.equals("<init>") && miNode.desc.equals("(Lnet/minecraft/server/MinecraftServer;Lyc;Ljava/lang/String;Lir;)V")) {
						//miNode.owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						//insnList[i] = new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc));
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
						System.out.println("Override ServerConfigurationManager createPlayerForUser");
					}
				}
			}

			ASMDebugUtils.log("AbstractInsnNode end");
			ASMDebugUtils.log("LocalVariableNode start");

			for (LocalVariableNode lvNode : (List<LocalVariableNode>)targetMethodNode.localVariables) {
				ASMDebugUtils.log(lvNode);
			}

			ASMDebugUtils.log("LocalVariableNode end");
		}

		targetMethodName = "a";// respawnPlayer
		targetMethodDesc = "(Liq;IZ)Liq;";//"(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";
		targetMethodNode = null;

		ASMDebugUtils.log("MethodNode start");

		for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
			ASMDebugUtils.log(mNode);

			if (targetMethodNode == null && targetMethodName.equals(mNode.name) && targetMethodDesc.equals(mNode.desc)) {
				targetMethodNode = mNode;
				ASMDebugUtils.log("found respawnPlayer");
				//break;
			}
		}

		ASMDebugUtils.log("MethodNode end");

		if (targetMethodNode != null) {
			ASMDebugUtils.log("AbstractInsnNode start");
			AbstractInsnNode[] insnList = targetMethodNode.instructions.toArray();

			for (int i = 0; i < insnList.length; i++) {//for (AbstractInsnNode aiNode : targetMethodNode.instructions.toArray()) {
				AbstractInsnNode aiNode = insnList[i];
				ASMDebugUtils.log(aiNode);

				if (aiNode instanceof TypeInsnNode) {
					TypeInsnNode tiNode = (TypeInsnNode)aiNode;

					if (tiNode.desc.equals("iq") && tiNode.getOpcode() == NEW) {
						tiNode.desc = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						System.out.println("Override TypeInsnNode iq to EntityPlayerSquidMP");
					}
				}

				if (aiNode instanceof MethodInsnNode) {
					MethodInsnNode miNode = (MethodInsnNode)aiNode;

					if (miNode.name.equals("<init>") && miNode.owner.equals("iq") && miNode.getOpcode() == INVOKESPECIAL && miNode.desc.equals("(Lnet/minecraft/server/MinecraftServer;Lyc;Ljava/lang/String;Lir;)V")) {
						//miNode.owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
						//insnList[i] = new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc));
						targetMethodNode.instructions.set(miNode, new MethodInsnNode(miNode.getOpcode(), "ayamitsu/urtsquid/player/EntityPlayerSquidMP", new String(miNode.name), new String(miNode.desc)));
						System.out.println("Override ServerConfigurationManager respawnPlayer");
					}
				}
			}

			ASMDebugUtils.log("AbstractInsnNode end");
			ASMDebugUtils.log("LocalVariableNode start");

			for (LocalVariableNode lvNode : (List<LocalVariableNode>)targetMethodNode.localVariables) {
				ASMDebugUtils.log(lvNode);
			}

			ASMDebugUtils.log("LocalVariableNode end");
		}

		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cNode.accept(cWriter);
		bytes = cWriter.toByteArray();

		try {
			this.exportToFile(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}

	private void exportToFile(byte[] arrayOfByte) throws IOException {
		File currentDir = (File)FMLInjectionData.data()[6];
		File exportFile = new File(currentDir, "gm.class");

		if (!exportFile.exists() && !exportFile.createNewFile()) {
			return;
		}

		DataOutputStream dos = new DataOutputStream(new FileOutputStream(exportFile));
		dos.write(arrayOfByte);
		dos.close();
	}

}
