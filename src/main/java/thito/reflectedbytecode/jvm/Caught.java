package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import thito.reflectedbytecode.*;

import java.lang.reflect.Type;

public class Caught extends BodyAccessor {

    private int local;

    public Caught(int local, Type type) {
        super(type);
        this.local = local;
    }

    public void printStackTrace() {
        method("printStackTrace").invokeVoid();
    }

    @Override
    public void write() {
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, local);
    }

}
