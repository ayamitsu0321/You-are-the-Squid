package ayamitsu.urtsquid.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by ayamitsu0321 on 2016/03/19.
 */
public abstract class TransformerBase implements IClassTransformer, Opcodes {

    private static boolean deobfuscatedEnvironment;

    public static boolean isDeobfuscated() {
        return deobfuscatedEnvironment;
    }

    static {
        boolean flag = false;

        try {
            Field field = CoreModManager.class.getField("deobfuscatedEnvironment");
            field.setAccessible(true);
            flag = field.getBoolean(null);
            field.setAccessible(false);
        } catch (Exception e) {
            flag = false;
        }

        deobfuscatedEnvironment = flag;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!isTarget(name, transformedName)) return bytes;

        System.out.println("Start transform: " + transformedName);

        return transformTarget(name, transformedName, bytes);
    }

    public abstract boolean isTarget(String name, String transformedName);

    public abstract byte[] transformTarget(String name, String transformedName, byte[] bytes);

    /*
        protected final ClassNode encode(byte[] bytes) {
            ClassNode cNode = new ClassNode();
            ClassReader cReader = new ClassReader(bytes);
            cReader.accept(cNode, 0);
            return cNode;
        }

        protected final byte[] decode(ClassNode cNode) {
            ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
                @Override
                public String getCommonSuperClass(String type1, String type2) {
                    return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important ... ?
                }
            };
            cNode.accept(cWriter);
            return cWriter.toByteArray();
        }
    */
    public static String mapClassName(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
    }

	public static String mapType(String type)
    {
		return FMLDeobfuscatingRemapper.INSTANCE.mapType(type);
	}

    public static String mapMethodName(String owner, String name, String desc) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
    }

    public static String mapMethodDesc(String desc) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
    }

    public static String mapFieldName(String owner, String name, String desc) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
    }

    public static String unmap(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }

    public static String unmapMethod(String owner, String name, String name_desc) {
        owner = unmap(owner);// unmap
        Map<String, String> methodMap = null;

        try {
            Method method = FMLDeobfuscatingRemapper.class.getDeclaredMethod("getMethodMap", new Class[] {String.class});
            method.setAccessible(true);
            methodMap = (Map<String, String>)method.invoke(FMLDeobfuscatingRemapper.INSTANCE, owner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : methodMap.entrySet()) {
            if (name.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return name_desc;
    }

    public static String unmapMethodName(String owner, String name) {
        owner = unmap(owner);// unmap
        Map<String, String> methodMap = null;

        try {
            Method method = FMLDeobfuscatingRemapper.class.getDeclaredMethod("getMethodMap", new Class[] {String.class});
            method.setAccessible(true);
            methodMap = (Map<String, String>)method.invoke(FMLDeobfuscatingRemapper.INSTANCE, owner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : methodMap.entrySet()) {
            if (name.equals(entry.getValue())) {
                // example:
                // g(III)Lahz -> g
                return entry.getKey().substring(0, entry.getKey().indexOf("("));
            }
        }

        return name;
    }

    public static String unmapMethodDesc(String owner, String name) {
        owner = unmap(owner);// unmap
        Map<String, String> methodMap = null;

        try {
            Method method = FMLDeobfuscatingRemapper.class.getDeclaredMethod("getMethodMap", new Class[] {String.class});
            method.setAccessible(true);
            methodMap = (Map<String, String>)method.invoke(FMLDeobfuscatingRemapper.INSTANCE, owner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : methodMap.entrySet()) {
            if (name.equals(entry.getValue())) {
                // example:
                // g(III)Lahz -> (III)Lahz
                return entry.getKey().substring(entry.getKey().indexOf("("), entry.getKey().length());
            }
        }

        return name;
    }

}
