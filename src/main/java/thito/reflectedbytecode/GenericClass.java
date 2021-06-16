package thito.reflectedbytecode;

import thito.reflectedbytecode.jvm.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class GenericClass extends AbstractClass implements IGenericClass {
    private IClass raw;
    private Generic genericClasses[];
    public GenericClass(Generic[] generics) {
        genericClasses = generics;
    }

    public GenericClass(ParameterizedType parameterizedType) {
        genericClasses = Arrays.stream(parameterizedType.getActualTypeArguments()).map(x -> new Generic(this, null)).toArray(Generic[]::new);
        raw = Java.Class(parameterizedType);
    }

    @Override
    public Generic[] getGenerics() {
        return genericClasses;
    }

    @Override
    public IConstructor[] getDeclaredConstructors() {
        return raw.getDeclaredConstructors();
    }

    @Override
    public IConstructor[] getConstructors() {
        return raw.getConstructors();
    }

    @Override
    public IMethod[] getDeclaredMethods() {
        return raw.getDeclaredMethods();
    }

    @Override
    public IMethod[] getMethods() {
        return raw.getMethods();
    }

    @Override
    public IField[] getDeclaredFields() {
        return raw.getDeclaredFields();
    }

    @Override
    public IField[] getFields() {
        return raw.getFields();
    }

    @Override
    public IClass[] getClasses() {
        return raw.getClasses();
    }

    @Override
    public IClass[] getDeclaredClasses() {
        return raw.getDeclaredClasses();
    }

    @Override
    public IClass getSuperClass() {
        return raw.getSuperClass();
    }

    @Override
    public IClass[] getInterfaces() {
        return raw.getInterfaces();
    }

    @Override
    public String getSimpleName() {
        return raw.getSimpleName();
    }

    @Override
    public IPackage getPackage() {
        return raw.getPackage();
    }

    @Override
    public boolean isPrimitive() {
        return raw.isPrimitive();
    }

    @Override
    public boolean isArray() {
        return raw.isArray();
    }

    @Override
    public boolean isInterface() {
        return raw.isInterface();
    }

    @Override
    public String getDescriptor() {
        return raw.getDescriptor() + "<" + Arrays.stream(genericClasses).map(x -> x.getSpecification().getDescriptor()).collect(Collectors.joining())+">";
    }

    @Override
    public String getRawName() {
        return raw.getRawName();
    }

    @Override
    public String getName() {
        return raw.getName();
    }

    @Override
    public int getModifiers() {
        return raw.getModifiers();
    }

    @Override
    public IClass getDeclaringClass() {
        return raw.getDeclaringClass();
    }

    @Override
    public String getTypeName() {
        return raw.getTypeName();
    }
}
