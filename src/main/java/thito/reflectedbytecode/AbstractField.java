package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

public abstract class AbstractField implements IField {

    public void set(Object instance, Object value) {
        Code code = Code.getCode();
        if (!Modifier.isStatic(getModifiers())) {
            Reference.handleWrite(instance);
        }
        Reference.handleWrite(value);
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitFieldInsn(
                Modifier.isStatic(getModifiers()) ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD,
                getDeclaringClass().getRawName(),
                getName(),
                getType().getDescriptor()
        );
    }

    public <T extends Reference> T get(Object instance) {
        return (T) (ArrayReference) () -> {
            Code code = Code.getCode();
            if (!Modifier.isStatic(getModifiers())) {
                Reference.handleWrite(instance);
            }
            MethodVisitor visitor = code.getCodeVisitor();
            visitor.visitFieldInsn(
                    Modifier.isStatic(getModifiers()) ? Opcodes.GETSTATIC : Opcodes.GETFIELD,
                    getDeclaringClass().getRawName(),
                    getName(),
                    getType().getDescriptor()
            );
        };
    }

}
