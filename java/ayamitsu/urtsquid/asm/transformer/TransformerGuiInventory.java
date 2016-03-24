package ayamitsu.urtsquid.asm.transformer;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by ayamitsu0321 on 2016/03/22.
 */
public class TransformerGuiInventory extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraft.client.gui.inventory.GuiInventory");
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

                if (("drawEntityOnScreen(IIIFFLnet/minecraft/entity/EntityLivingBase;)V").equals(deobfMethod)) {
                    methodVisitor = new MethodAdapter(ASM4, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                            String deobfOwner = mapClassName(owner);
                            String deobfMethodName = mapMethodName(owner, name, desc);

                            /** コードの追加
                             * GlStateManager.translate(0.0F, 1.2F, 0.0F)
                             */
                            if (!this.endExcute && opcode == INVOKESTATIC && ("net/minecraft/client/renderer/GlStateManager").equals(deobfOwner) && ("translate".equals(deobfMethodName))) {
                                this.foundAmout++;

                                if (this.foundAmout >= 2) {

                                    super.visitInsn(FCONST_0);
                                    super.visitLdcInsn(Float.valueOf(1.2F));// 1.2F: magic number
                                    super.visitInsn(FCONST_0);

                                    super.visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
                                    this.endExcute = true;
                                }
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

        public int foundAmout = 0;
        public boolean endExcute = false;

        public MethodAdapter(int api, MethodVisitor mv) {
            super(api, mv);
        }

    }

}
