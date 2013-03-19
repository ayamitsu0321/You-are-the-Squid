package ayamitsu.urtsquid.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IClassTransformer;

public abstract class TransformerBase implements IClassTransformer, Opcodes {

	@Override
	public abstract byte[] transform(String name, String transformedName, byte[] bytes);

	protected final ClassNode encode(byte[] bytes) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(bytes);
		cReader.accept(cNode, 0);
		return cNode;
	}

	protected final byte[] decode(ClassNode cNode) {
		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)
		{
			@Override
			public String getCommonSuperClass(String type1, String type2)
			{
				return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important ... ?
			}
		};
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}

	protected String map(String typeName)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
	}

	/*protected String mapType(String type)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapType(type);
	}*/

	protected String mapMethodName(String owner, String name, String desc)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
	}

	protected String mapMethodDesc(String desc)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
	}

	protected String mapFieldName(String owner, String name, String desc)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
	}

}
