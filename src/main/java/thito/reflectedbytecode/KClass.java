package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.WeakHashMap;

public class KClass extends AbstractClass implements IClass {

    static final WeakHashMap<Class<?>, KClass> cache = new WeakHashMap<>(); // should thread local?

    private IConstructor[] declaredConstructors, constructors;
    private IMethod[] declaredMethods, methods;
    private IField[] declaredFields, fields;
    private IClass superclass, declaringClass;
    private IClass[] interfaces, declaredClasses, classes;
    private Class<?> clazz;
    public KClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getDelegated() {
        return clazz;
    }

    @Override
    public IClass[] getClasses() {
        return classes == null ?
                classes = Arrays.stream(clazz.getClasses()).map(IClass::fromClass).toArray(IClass[]::new) :
                classes;
    }

    @Override
    public IClass[] getDeclaredClasses() {
        return declaredClasses == null ?
                declaredClasses = Arrays.stream(clazz.getDeclaredClasses()).map(IClass::fromClass).toArray(IClass[]::new) :
                declaredClasses;
    }

    @Override
    public IConstructor[] getDeclaredConstructors() {
        return declaredConstructors == null ?
                declaredConstructors = Arrays.stream(clazz.getDeclaredConstructors()).map(KConstructor::new).toArray(IConstructor[]::new) :
                declaredConstructors;
    }

    @Override
    public IConstructor[] getConstructors() {
        return constructors == null ?
                constructors = Arrays.stream(clazz.getConstructors()).map(KConstructor::new).toArray(IConstructor[]::new) :
                constructors;
    }

    @Override
    public IMethod[] getDeclaredMethods() {
        return declaredMethods == null ?
                declaredMethods = Arrays.stream(clazz.getDeclaredMethods()).map(KMethod::new).toArray(IMethod[]::new) :
                declaredMethods;
    }

    @Override
    public IMethod[] getMethods() {
        return methods == null ?
                methods = Arrays.stream(clazz.getMethods()).map(KMethod::new).toArray(IMethod[]::new) :
                methods;
    }
    @Override
    public IField[] getDeclaredFields() {
        return declaredFields == null ?
                declaredFields = Arrays.stream(clazz.getDeclaredFields()).map(KField::new).toArray(IField[]::new) :
                declaredFields;
    }

    @Override
    public IField[] getFields() {
        return fields == null ?
                fields = Arrays.stream(clazz.getFields()).map(KField::new).toArray(IField[]::new) :
                fields;
    }

    @Override
    public IClass getSuperClass() {
        return superclass == null ? superclass = IClass.fromClass(clazz.getSuperclass()) : superclass;
    }

    @Override
    public IClass[] getInterfaces() {
        return interfaces == null ?
                interfaces = Arrays.stream(clazz.getInterfaces()).map(IClass::fromClass).toArray(IClass[]::new) :
                interfaces;
    }

    @Override
    public boolean isPrimitive() {
        return clazz.isPrimitive();
    }

    @Override
    public boolean isArray() {
        return clazz.isArray();
    }

    @Override
    public String getDescriptor() {
        return Type.getDescriptor(clazz);
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public int getModifiers() {
        return clazz.getModifiers();
    }

    @Override
    public IClass getDeclaringClass() {
        return declaringClass == null ? declaringClass = IClass.fromClass(clazz.getDeclaringClass()) : declaringClass;
    }

    @Override
    public String getRawName() {
        return Type.getInternalName(clazz);
    }

    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public IPackage getPackage() {
        return new Package(clazz.getPackage());
    }
}
