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

    @Override
    public void invokeVoid(Object instance, Object... args) {
        if (getParameterCount() != args.length) throw new IllegalArgumentException("invalid argument length expected "+getParameterCount()+" but found "+args.length);
        if (instance != null && !Modifier.isStatic(getModifiers())) {
            Reference.handleWrite(getDeclaringClass(), instance);
        }
        Code code = Code.getCode();
        IClass[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                Reference.handleWrite(parameterTypes[i], args[i]);
            } catch (Throwable t) {
                throw new IllegalArgumentException("Invalid argument: "+i+" ("+getDeclaringClass().getName()+"#"+getName()+")", t);
            }
        }
        MethodVisitor visitor = code.getCodeVisitor();
        int operation;
        if (Modifier.isPrivate(getModifiers())) {
            operation = Opcodes.INVOKESPECIAL;
        } else if (instance == null || Modifier.isStatic(getModifiers())) {
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
        if (getReturnType() != null && !getReturnType().getDescriptor().equals("V")) {
            visitor.visitInsn(Opcodes.POP);
        }
    }

    public Reference invoke(Object instance, Object... args) {
        if (getParameterCount() != args.length) throw new IllegalArgumentException("invalid argument length expected "+getParameterCount()+" but found "+args.length);
        if (getReturnType() == null || getReturnType().getDescriptor().equals("V")) {
            invokeVoid(instance, args);
            return null;
        }
        return new Reference(getReturnType()) {
            @Override
            protected void write() {
                Code code = Code.getCode();
                if (instance != null && !Modifier.isStatic(getModifiers())) {
                    Reference.handleWrite(getDeclaringClass(), instance);
                }
                IClass[] parameterTypes = getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    try {
                        Reference.handleWrite(parameterTypes[i], args[i]);
                    } catch (Throwable t) {
                        throw new IllegalArgumentException("Invalid argument: "+i+" ("+getDeclaringClass().getName()+"#"+getName()+")", t);
                    }
                }
                MethodVisitor visitor = code.getCodeVisitor();
                int operation;
                if (Modifier.isPrivate(getModifiers())) {
                    operation = Opcodes.INVOKESPECIAL;
                } else if (instance == null || Modifier.isStatic(getModifiers())) {
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
            }
        };
    }

}
