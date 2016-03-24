package ayamitsu.urtsquid.asm.transformer;


import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.*;

/**
 * Created by ayamitsu0321 on 2016/03/19.
 */
public class TransformerGuiIngameForge extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraftforge.client.GuiIngameForge");
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

                // protected void renderAir(int width, int height)
                if (("renderAir(II)V").equals(deobfMethod)) {
                    methodVisitor = new MethodAdapter(ASM4, methodVisitor) {

                        //if (player.isInsideOfMaterial(Material.water)) -> if (!player.isInsideOfMaterial(Material.water))
                        @Override
                        public void visitJumpInsn(int opcode, Label label) {
                            if (opcode == IFEQ) {
                                if (flag) {
                                    super.visitJumpInsn(IFNE, label);
                                    endExcute = true;
                                    return;
                                }

                                flag = true;
                            }

                            super.visitJumpInsn(opcode, label);
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

        //public boolean foundTargetMethod = false;
        public boolean flag = false;
        public boolean endExcute = false;

        public MethodAdapter(int api, MethodVisitor mv) {
            super(api, mv);
        }

        public MethodAdapter(int api) {
            super(api);
        }

    }

}
