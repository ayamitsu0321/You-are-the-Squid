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
public class TransformerPlayerList extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraft.server.management.PlayerList");
    }

    @Override
    public byte[] transformTarget(String name, String transformedName, byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS) {
            @Override
            public String getCommonSuperClass(String type1, String type2) {
                return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important ... ?
            }
        };

        ClassVisitor classVisitor = new ClassAdapter(name, classWriter) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                Set<String> targetMethodNames_0 = Sets.newHashSet("func_148545_a", "createPlayerForUser");
                String targetMethodDesc_0 = "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/entity/player/EntityPlayerMP;";
                Set<String> targetMethodNames_1 = Sets.newHashSet("func_72368_a", "recreatePlayerEntity");
                String targetMethodDesc_1 = "(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";
                String deobfName = mapMethodName(owner, name, desc);
                String deobfDesc = mapMethodDesc(desc);

                if (targetMethodNames_0.contains(deobfName) && targetMethodDesc_0.equals(deobfDesc)) {
                    methodVisitor = new MethodVisitor(ASM4, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            String deobfOwner = mapClassName(owner);
                            String deobfMethodName = mapMethodName(owner, name, desc);

                            if (opcode == INVOKESPECIAL && ("net/minecraft/entity/player/EntityPlayerMP").equals(deobfOwner) && ("<init>".equals(deobfMethodName))) {
                                owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                                return;
                            }

                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                        }

                        @Override
                        public void visitTypeInsn(int opcode, String type) {
                            if (opcode == NEW && ("net/minecraft/entity/player/EntityPlayerMP").equals(mapType(type))) {
                                super.visitTypeInsn(opcode, "ayamitsu/urtsquid/player/EntityPlayerSquidMP");
                                return;
                            }

                            super.visitTypeInsn(opcode, type);
                        }

                    };
                } else if (targetMethodNames_1.contains(deobfName) && targetMethodDesc_1.equals(deobfDesc)) {
                    methodVisitor = new MethodVisitor(ASM4, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            String deobfOwner = mapClassName(owner);
                            String deobfMethodName = mapMethodName(owner, name, desc);

                            if (opcode == INVOKESPECIAL && ("net/minecraft/entity/player/EntityPlayerMP").equals(deobfOwner) && ("<init>".equals(deobfMethodName))) {
                                owner = "ayamitsu/urtsquid/player/EntityPlayerSquidMP";
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                                return;
                            }

                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                        }

                        @Override
                        public void visitTypeInsn(int opcode, String type) {
                            if (opcode == NEW && ("net/minecraft/entity/player/EntityPlayerMP").equals(mapType(type))) {
                                super.visitTypeInsn(opcode, "ayamitsu/urtsquid/player/EntityPlayerSquidMP");
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
