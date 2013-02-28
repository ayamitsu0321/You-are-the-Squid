package ayamitsu.urtsquid.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.relauncher.IClassTransformer;

public abstract class TransformerBase implements IClassTransformer, Opcodes {

	@Override
	public abstract byte[] transform(String name, byte[] bytes);

	protected final ClassNode encode(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);
		return cNode;
	}

	protected final byte[] decode(ClassNode cNode) {
		// ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS
		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}

}
