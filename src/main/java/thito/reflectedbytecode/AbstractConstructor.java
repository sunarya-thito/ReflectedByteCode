package thito.reflectedbytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

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

    public Reference newInstance(Object... args) {
        if (getParameterCount() != args.length) throw new IllegalArgumentException("invalid argument length expected "+getParameterCount()+" but found "+args.length);
        return new Reference(getDeclaringClass()) {
            @Override
            protected void write() {
                Code code = Code.getCode();
                MethodVisitor visitor = code.getCodeVisitor();
                visitor.visitTypeInsn(Opcodes.NEW, getDeclaringClass().getRawName());
                visitor.visitInsn(Opcodes.DUP);
                IClass[] types = getParameterTypes();
                for (int i = 0; i < args.length; i++) {
                    try {
                        Reference.handleWrite(types[i], args[i]);
                    } catch (Throwable t) {
                        throw new IllegalArgumentException("Invalid argument: "+i+" ("+getDeclaringClass().getName()+"#"+getName()+")", t);
                    }
                }
                visitor.visitMethodInsn(
                        Opcodes.INVOKESPECIAL,
                        getDeclaringClass().getRawName(),
                        "<init>",
                        getMethodDescriptor(),
                        false
                );
            }
        };
    }

    @Override
    public void newInstanceVoid(Object... args) {
        if (getParameterCount() != args.length) throw new IllegalArgumentException("invalid argument length expected "+getParameterCount()+" but found "+args.length);
        Code code = Code.getCode();
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitTypeInsn(Opcodes.NEW, getDeclaringClass().getRawName());
//        visitor.visitInsn(Opcodes.DUP);
        IClass[] types = getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            try {
                Reference.handleWrite(types[i], args[i]);
            } catch (Throwable t) {
                throw new IllegalArgumentException("Invalid argument: "+i+" ("+getDeclaringClass().getName()+"#"+getName()+")", t);
            }
        }
        visitor.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                getDeclaringClass().getRawName(),
                "<init>",
                getMethodDescriptor(),
                false
        );
//        visitor.visitInsn(Opcodes.POP);
    }

    @Override
    public String getName() {
        return "<init>";
    }

    @Override
    public String toString() {
        return Modifier.toString(getModifiers())+" "+getName()+"("+ Arrays.stream(getParameterTypes()).map(IClass::getName).collect(Collectors.joining(", "))+")void";
    }

}
