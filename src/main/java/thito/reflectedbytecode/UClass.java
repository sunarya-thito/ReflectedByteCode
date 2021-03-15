package thito.reflectedbytecode;

import java.lang.reflect.*;
import java.util.*;

public class UClass implements IClass {

    public static UClass Class(String name) {
        return new UClass(name);
    }

    private String name;

    public UClass(String name) {
        this.name = name;
    }

    @Override
    public Optional<UConstructor> getDeclaredConstructor(Type... parameterTypes) {
        return Optional.of(new UConstructor(this, parameterTypes));
    }

    @Override
    public Optional<UConstructor> getConstructor(Type... parameterTypes) {
        return Optional.of(new UConstructor(this, parameterTypes));
    }

    @Override
    public IConstructor[] getDeclaredConstructors() {
        return new IConstructor[0];
    }

    @Override
    public IConstructor[] getConstructors() {
        return new IConstructor[0];
    }

    @Override
    public Optional<UMethod> getDeclaredMethod(String name, Type... parameterTypes) {
        return Optional.of(new UMethod(this, name, Modifier.PRIVATE, parameterTypes));
    }

    @Override
    public Optional<UMethod> getMethod(String name, Type... parameterTypes) {
        return Optional.of(new UMethod(this, name, Modifier.PUBLIC, parameterTypes));
    }

    @Override
    public IMethod[] getDeclaredMethods() {
        return new IMethod[0];
    }

    @Override
    public IMethod[] getMethods() {
        return new IMethod[0];
    }

    @Override
    public Optional<UField> getDeclaredField(String name) {
        return Optional.of(new UField(this, name));
    }

    @Override
    public Optional<UField> getField(String name) {
        return Optional.of(new UField(this, name));
    }

    @Override
    public IField[] getDeclaredFields() {
        return new IField[0];
    }

    @Override
    public IField[] getFields() {
        return new IField[0];
    }

    @Override
    public IClass[] getClasses() {
        return new IClass[0];
    }

    @Override
    public IClass[] getDeclaredClasses() {
        return new IClass[0];
    }

    @Override
    public IClass getSuperClass() {
        return null;
    }

    @Override
    public IClass[] getInterfaces() {
        return new IClass[0];
    }

    @Override
    public String getSimpleName() {
        String name = getName();
        int index = name.indexOf('.');
        if (index >= 0) {
            name = name.substring(index + 1);
        }
        return name;
    }

    @Override
    public IPackage getPackage() {
        String name = getName();
        int index = name.indexOf('.');
        if (index >= 0) {
            name = name.substring(0, index);
        }
        return new Package(name);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String getDescriptor() {
        return "L"+getRawName()+";";
    }

    @Override
    public String getRawName() {
        return getName().replace('.', '/');
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public IClass getDeclaringClass() {
        return null;
    }
}
