package ayamitsu.urtsquid.asm.transformer;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class TransformerEntityRenderer extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraft.client.renderer.EntityRenderer");
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

        ClassVisitor classVisitor = new ClassAdapter(transformedName, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                String deobfName = mapMethodName(owner, name, desc);
                String deobfDesc = mapMethodDesc(desc);
                String deobfMethod = deobfName + deobfDesc;

                if (("orientCamera(F)V").equals(deobfMethod)) {
                    methodVisitor = new MethodAdapter(ASM4, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                            String deobfOwner = mapClassName(owner);
                            String deobfMethodName = mapMethodName(owner, name, desc);

                            /** コードの追加
                             * GlStateManager.translate(0.0F, entity.getEyeHeight() - 1.62F, 0.0F)
                             */
                            if (!this.endExcute && opcode == INVOKESTATIC && ("net/minecraft/client/renderer/GlStateManager").equals(deobfOwner) && ("translate".equals(deobfMethodName))) {
                                super.visitInsn(FCONST_0);

                                /** entity.getEyeHeight() - 1.62 */
                                super.visitLdcInsn(Float.valueOf(1.0F));
                                super.visitVarInsn(ALOAD, 2);// Minecraft#getRenderViewEntity
                                super.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "getEyeHeight", "()F", false);// Entity#getEyeHeight
                                super.visitInsn(FSUB);

                                super.visitInsn(FCONST_0);

                                super.visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
                                this.endExcute = true;
                            }
                        }

                    };
                }

                return methodVisitor;
            }
        };

        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

    private static class MethodAdapter extends MethodVisitor {

        public boolean endExcute = false;

        public MethodAdapter(int api, MethodVisitor mv) {
            super(api, mv);
        }

    }

}
