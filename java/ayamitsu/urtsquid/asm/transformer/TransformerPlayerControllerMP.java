package ayamitsu.urtsquid.asm.transformer;

import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.Set;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class TransformerPlayerControllerMP extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraft.client.multiplayer.PlayerControllerMP");
    }

    @Override
    public byte[] transformTarget(String name, String transformedName, byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
            @Override
            public String getCommonSuperClass(String type1, String type2) {
                return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important ... ?
            }
        };

        ClassVisitor classVisitor = new ClassAdapter(name, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                Set<String> targetMethodNames = Sets.newHashSet("func_178892_a", "createClientPlayer");
                String targetMethodDesc = "(Lnet/minecraft/world/World;Lnet/minecraft/stats/StatFileWriter;)Lnet/minecraft/client/entity/EntityPlayerSP;";
                String deobfName = mapMethodName(owner, name, desc);
                String deobfDesc = mapMethodDesc(desc);

                if (targetMethodNames.contains(deobfName) && targetMethodDesc.equals(deobfDesc)) {
                    methodVisitor = new MethodVisitor(ASM4, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            String deobfOwner = mapClassName(owner);
                            String deobfMethodName = mapMethodName(owner, name, desc);

                            if (opcode == INVOKESPECIAL && ("net/minecraft/client/entity/EntityPlayerSP").equals(deobfOwner) && ("<init>".equals(deobfMethodName))) {
                                owner = "ayamitsu/urtsquid/player/EntityPlayerSquidSP";
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                                return;
                            }

                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                        }

                        @Override
                        public void visitTypeInsn(int opcode, String type) {
                            if (opcode == NEW && ("net/minecraft/client/entity/EntityPlayerSP").equals(mapType(type))) {
                                super.visitTypeInsn(opcode, "ayamitsu/urtsquid/player/EntityPlayerSquidSP");
                                return;
                            }

                            super.visitTypeInsn(opcode, type);
                        }

                    };
                }

                return methodVisitor;
            }
        };

        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

}
