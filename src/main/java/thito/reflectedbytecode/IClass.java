package thito.reflectedbytecode;

import java.lang.reflect.Modifier;
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
        throw new UnsupportedOperationException("unknown type");
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
        return null;
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

    // NATIVE IMPLEMENTATION
    String getDescriptor();
    String getRawName();
}
