package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

public abstract class AbstractMethod implements IMethod {

    protected String getMethodDescriptor() {
        StringBuilder builder = new StringBuilder("()");
        IClass[] parameterTypes = getParameterTypes();
        for (int i = parameterTypes.length - 1; i >= 0; i--) {
            builder.insert(1, parameterTypes[i].getDescriptor());
        }
        IClass returnType = getReturnType();
        if (returnType == null) {
            builder.append("V");
        } else {
            builder.append(returnType.getDescriptor());
        }
        return builder.toString();
    }

    public <T extends Reference> T invoke(Object instance, Object... args) {
        Code code = Code.getCode();
        boolean isVoid = getReturnType() == null || getReturnType().getDescriptor().equals("V");
        int localIndex = isVoid ? 0 : code.requestLocalIndex();
        if (!Modifier.isStatic(getModifiers())) {
            Reference.handleWrite(instance);
        }
        for (Object arg : args) {
            Reference.handleWrite(arg);
        }
        MethodVisitor visitor = code.getCodeVisitor();
        int operation;
        if (Modifier.isPrivate(getModifiers())) {
            operation = Opcodes.INVOKESPECIAL;
        } else if (Modifier.isStatic(getModifiers())) {
            operation = Opcodes.INVOKESTATIC;
        } else {
            if (Modifier.isInterface(getDeclaringClass().getModifiers())) {
                operation = Opcodes.INVOKEINTERFACE;
            } else {
                operation = Opcodes.INVOKEVIRTUAL;
            }
        }
        visitor.visitMethodInsn(
                operation,
                getDeclaringClass().getRawName(),
                getName(),
                getMethodDescriptor(),
                operation == Opcodes.INVOKEINTERFACE);
        if (!isVoid) {
            visitor.visitVarInsn(Opcodes.ASTORE, localIndex);
        }
        return isVoid ? null : (T) (ArrayReference) () -> {
            Code code2 = Code.getCode();
            code2.getCodeVisitor().visitVarInsn(Opcodes.ALOAD, localIndex);
        };
    }

}
