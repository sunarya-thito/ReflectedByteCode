package thito.reflectedbytecode;

import thito.reflectedbytecode.jvm.*;

public class GGeneric extends AbstractClass implements IGeneric {
    private IClass extension;
    private GClass parameterizedType;

    public GGeneric(GClass parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    @Override
    public IClass getExtension() {
        return extension == null ? Java.Class(Object.class) : extension;
    }

    @Override
    public GClass getDeclaringClass() {
        return parameterizedType;
    }

    public GGeneric setExtension(IClass extension) {
        this.extension = extension;
        return this;
    }

    private IClass preferredClass() {
        return extension == null ? Java.Class(Object.class) : extension;
    }

    @Override
    public IConstructor[] getDeclaredConstructors() {
        return preferredClass().getDeclaredConstructors();
    }

    @Override
    public IConstructor[] getConstructors() {
        return preferredClass().getConstructors();
    }

    @Override
    public IMethod[] getDeclaredMethods() {
        return preferredClass().getDeclaredMethods();
    }

    @Override
    public IMethod[] getMethods() {
        return preferredClass().getMethods();
    }

    @Override
    public IField[] getDeclaredFields() {
        return preferredClass().getDeclaredFields();
    }

    @Override
    public IField[] getFields() {
        return preferredClass().getFields();
    }

    @Override
    public IClass[] getClasses() {
        return preferredClass().getClasses();
    }

    @Override
    public IClass[] getDeclaredClasses() {
        return preferredClass().getDeclaredClasses();
    }

    @Override
    public IClass getSuperClass() {
        return preferredClass().getSuperClass();
    }

    @Override
    public IClass[] getInterfaces() {
        return preferredClass().getInterfaces();
    }

    @Override
    public String getSimpleName() {
        return preferredClass().getSimpleName();
    }

    @Override
    public IPackage getPackage() {
        return preferredClass().getPackage();
    }

    @Override
    public boolean isPrimitive() {
        return preferredClass().isPrimitive();
    }

    @Override
    public boolean isArray() {
        return preferredClass().isArray();
    }

    @Override
    public String getDescriptor() {
        return preferredClass().getDescriptor();
    }

    @Override
    public String getRawName() {
        return preferredClass().getRawName();
    }

    @Override
    public String getName() {
        return preferredClass().getName();
    }

    @Override
    public int getModifiers() {
        return preferredClass().getModifiers();
    }
}
