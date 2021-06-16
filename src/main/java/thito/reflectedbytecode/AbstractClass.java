package thito.reflectedbytecode;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractClass implements IClass {
    @Override
    public Optional<IConstructor> getDeclaredConstructor(Type... parameterTypes) {
        return Arrays.stream(getDeclaredConstructors()).filter(element -> {
            IClass[] params = element.getParameterTypes();
            if (params.length != parameterTypes.length) return false;
            for (int i = 0; i < params.length; i++) {
                if (!parameterTypes[i].getTypeName().equals(params[i].getTypeName())) {
                    return false;
                }
            }
            return true;
        }).findFirst();
    }

    @Override
    public Optional<IConstructor> getConstructor(Type... parameterTypes) {
        return Arrays.stream(getConstructors()).filter(element -> {
            IClass[] params = element.getParameterTypes();
            if (params.length != parameterTypes.length) return false;
            for (int i = 0; i < params.length; i++) {
                if (!parameterTypes[i].getTypeName().equals(params[i].getTypeName())) {
                    return false;
                }
            }
            return true;
        }).findFirst();
    }

    @Override
    public Optional<IMethod> getDeclaredMethod(String name, Type... parameterTypes) {
        return Arrays.stream(getDeclaredMethods()).filter(element -> {
            if (!element.getName().equals(name)) return false;
            IClass[] params = element.getParameterTypes();
            if (params.length != parameterTypes.length) return false;
            for (int i = 0; i < params.length; i++) {
                if (!parameterTypes[i].getTypeName().equals(params[i].getTypeName())) {
                    return false;
                }
            }
            return true;
        }).findFirst();
    }

    @Override
    public Optional<IMethod> getMethod(String name, Type... parameterTypes) {
        return Arrays.stream(getMethods()).filter(element -> {
            if (!element.getName().equals(name)) return false;
            IClass[] params = element.getParameterTypes();
            if (params.length != parameterTypes.length) return false;
            for (int i = 0; i < params.length; i++) {
                if (!parameterTypes[i].getTypeName().equals(params[i].getTypeName())) {
                    return false;
                }
            }
            return true;
        }).findFirst();
    }

    @Override
    public Optional<IField> getDeclaredField(String name) {
        return Arrays.stream(getDeclaredFields()).filter(element -> element.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<IField> getField(String name) {
        return Arrays.stream(getFields()).filter(element -> element.getName().equals(name)).findFirst();
    }

    @Override
    public String getTypeName() {
        return getName();
    }
}
