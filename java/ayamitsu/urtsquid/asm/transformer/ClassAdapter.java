package ayamitsu.urtsquid.asm.transformer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by ayamitsu0321 on 2016/03/20.
 */
public class ClassAdapter extends ClassVisitor {

    public String owner;

    public ClassAdapter(String owner, ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
        this.owner = owner;
    }

}
