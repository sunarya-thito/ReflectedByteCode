package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class AbstractConstructor implements IConstructor {

    protected String getMethodDescriptor() {
        StringBuilder builder = new StringBuilder("()V");
        IClass[] parameterTypes = getParameterTypes();
        for (int i = parameterTypes.length - 1; i >= 0; i--) {
            builder.insert(1, parameterTypes[i].getDescriptor());
        }
        return builder.toString();
    }

    abstract void invoke(Object...args);

    public <T extends Reference> T newInstance(Object... args) {
        return (T) (ArrayReference) () -> {
            Code code = Code.getCode();
            MethodVisitor visitor = code.getCodeVisitor();
            visitor.visitTypeInsn(Opcodes.NEW, getDeclaringClass().getDescriptor());
            visitor.visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    getDeclaringClass().getRawName(),
                    "<init>",
                    getMethodDescriptor(),
                    false
                    );
        };
    }

    @Override
    public String getName() {
        return "<init>";
    }
}
