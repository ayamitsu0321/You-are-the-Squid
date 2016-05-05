package ayamitsu.urtsquid.asm.transformer;

import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.*;

import java.util.Set;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class TransformerBlockBed extends TransformerBase {

    @Override
    public boolean isTarget(String name, String transformedName) {
        return transformedName.equals("net.minecraft.block.BlockBed");
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

                Set<String> targetMethodNames = Sets.newHashSet("func_176469_d", "hasRoomForPlayer");
                String targetMethodDesc = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z";
                String deobfName = mapMethodName(owner, name, desc);
                String deobfDesc = mapMethodDesc(desc);

                if (targetMethodNames.contains(deobfName) && targetMethodDesc.equals(deobfDesc)) {
                    methodVisitor = new MethodVisitor(ASM4, methodVisitor) {

                        /** コードを丸ごと書き換え
                         *
                         *  protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
                         *  {
                         *      return worldIn.getBlockState(pos.down()).isFullyOpaque() && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
                         *  }
                         *
                         *                  ↓
                         *
                         *  protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
                         *  {
                         *      return worldIn.getBlockState(pos.down()).isFullyOpaque() && !worldIn.getBlockState(pos).getMaterial().isSolid();
                         *  }
                         *
                         */
                        @Override
                        public void visitCode() {
                            super.visitCode();

                            Label l0 = new Label();
                            mv.visitLabel(l0);
                            mv.visitLineNumber(221, l0);
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitVarInsn(ALOAD, 1);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", TransformerBase.isDeobfuscated() ? "down" : "func_177977_b", "()Lnet/minecraft/util/math/BlockPos;", false);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", TransformerBase.isDeobfuscated() ? "getBlockState" : "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
                            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", TransformerBase.isDeobfuscated() ? "isFullyOpaque" : "func_185896_q", "()Z", true);
                            Label l1 = new Label();
                            mv.visitJumpInsn(IFEQ, l1);
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitVarInsn(ALOAD, 1);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", TransformerBase.isDeobfuscated() ? "getBlockState" : "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
                            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", TransformerBase.isDeobfuscated() ? "getMaterial" : "func_185904_a", "()Lnet/minecraft/block/material/Material;", true);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/material/Material", TransformerBase.isDeobfuscated() ? "isSolid" : "func_76220_a", "()Z", false);
                            mv.visitJumpInsn(IFNE, l1);
                            /*
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitVarInsn(ALOAD, 1);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "up", "()Lnet/minecraft/util/math/BlockPos;", false);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "getBlockState", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
                            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "getMaterial", "()Lnet/minecraft/block/material/Material;", true);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/material/Material", "isSolid", "()Z", false);
                            mv.visitJumpInsn(IFNE, l1);
                            */
                            mv.visitInsn(ICONST_1);
                            Label l2 = new Label();
                            mv.visitJumpInsn(GOTO, l2);
                            mv.visitLabel(l1);
                            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                            mv.visitInsn(ICONST_0);
                            mv.visitLabel(l2);
                            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
                            mv.visitInsn(IRETURN);
                        }

                        //@Override
                        //public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {}

                    };
                }

                return methodVisitor;
            }
        };

        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

}
