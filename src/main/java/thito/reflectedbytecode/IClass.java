package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.Optional;

public interface IClass extends IMember, Type {

    static IClass fromClass(Type type) {
        if (type instanceof IClass) {
            return (IClass) type;
        }
        if (type instanceof Class) {
            KClass kClass = new KClass((Class<?>) type);
            KClass.cache.put((Class<?>) type, kClass);
            return kClass;
        }
        if (type instanceof ParameterizedType) {
            GenericClass parameterizedClass = new GenericClass((ParameterizedType) type);
            return parameterizedClass;
        }
        return fromClass(Object.class);
//        throw new UnsupportedOperationException("unknown type");
    }
    static IClass findClass(String name) {
        GClass generatedClass = Context.getContext().getClass(name);
        if (generatedClass != null) {
            return generatedClass;
        }
        try {
            Class<?> caller = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            Class<?> runtimeClass = Class.forName(name, false, caller.getClassLoader());
            return fromClass(runtimeClass);
        } catch (Throwable t) {
        }
        try {
            Class<?> runtimeClass = Class.forName(name, false, Thread.currentThread().getContextClassLoader());
            return fromClass(runtimeClass);
        } catch (Throwable t) {
        }
        return new UClass(name);
    }
    Optional<? extends IConstructor> getDeclaredConstructor(Type... parameterTypes);
    Optional<? extends IConstructor> getConstructor(Type... parameterTypes);
    IConstructor[] getDeclaredConstructors();
    IConstructor[] getConstructors();
    Optional<? extends IMethod> getDeclaredMethod(String name, Type... parameterTypes);
    Optional<? extends IMethod> getMethod(String name, Type... parameterTypes);
    IMethod[] getDeclaredMethods();
    IMethod[] getMethods();
    Optional<? extends IField> getDeclaredField(String name);
    Optional<? extends IField> getField(String name);
    IField[] getDeclaredFields();
    IField[] getFields();
    IClass[] getClasses();
    IClass[] getDeclaredClasses();
    IClass getSuperClass();
    IClass[] getInterfaces();
    String getSimpleName();
    IPackage getPackage();
    boolean isPrimitive();
    boolean isArray();
    default boolean isInterface() {
        return Modifier.isInterface(getModifiers());
    }
    default Reference newInstance() {
        int constructors = getConstructors().length;
        Optional<? extends IConstructor> constructor = getConstructor();
        if (constructor.isPresent()) {
            return constructor.get().newInstance();
        } else {
            return new Reference(this) {
                @Override
                protected void write() {
                    MethodVisitor visitor = Code.getCode().getCodeVisitor();
                    visitor.visitTypeInsn(Opcodes.NEW, getRawName());
                    visitor.visitInsn(Opcodes.DUP);
                    visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, getRawName(), "<init>", "()V", false);
                }
            };
        }
    }

    default Reference staticField(String name) {
        return getField(name).get().get(null);
    }
    default Reference.MethodInvocation staticMethod(String name, Type... types) {
        return new Reference.MethodInvocation() {
            @Override
            public Reference invoke(Object... args) {
                return getMethod(name, types).get().invoke(null, args);
            }

            @Override
            public void invokeVoid(Object... args) {
                getMethod(name, types).get().invokeVoid(null, args);
            }
        };
    }

    // NATIVE IMPLEMENTATION
    String getDescriptor();
    String getRawName();
}
