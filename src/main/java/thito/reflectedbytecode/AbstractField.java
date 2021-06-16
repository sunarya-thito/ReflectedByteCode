package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

public abstract class AbstractField implements IField {

    public void set(Object instance, Object value) {
        Code code = Code.getCode();
        if (!Modifier.isStatic(getModifiers())) {
            Reference.handleWrite(getDeclaringClass(), instance);
        }
        Reference.handleWrite(getType(), value);
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitFieldInsn(
                Modifier.isStatic(getModifiers()) ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD,
                getDeclaringClass().getRawName(),
                getName(),
                getType().getDescriptor()
        );
    }

    public Reference get(Object instance) {
        return new Reference(getType()) {
            @Override
            public void write() {
                Code code = Code.getCode();
                if (instance != null && !Modifier.isStatic(getModifiers())) {
                    Reference.handleWrite(getDeclaringClass(), instance);
                }
                MethodVisitor visitor = code.getCodeVisitor();
                visitor.visitFieldInsn(
                        instance == null || Modifier.isStatic(getModifiers()) ? Opcodes.GETSTATIC : Opcodes.GETFIELD,
                        getDeclaringClass().getRawName(),
                        getName(),
                        AbstractField.this.getType().getDescriptor()
                );
            }
        };
    }

}
