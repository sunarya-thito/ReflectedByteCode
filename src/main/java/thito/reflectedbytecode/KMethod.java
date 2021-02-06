package thito.reflectedbytecode;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class KMethod extends AbstractMethod implements IMethod {
    private Method method;
    private IClass[] parameterTypes, Throws;
    private IClass declaringClass, returnType;

    public KMethod(Method method) {
        this.method = method;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    @Override
    public IClass getDeclaringClass() {
        return declaringClass == null ? declaringClass = IClass.fromClass(method.getDeclaringClass()) : declaringClass;
    }

    @Override
    public IClass[] getParameterTypes() {
        return parameterTypes == null ?
                parameterTypes = Arrays.stream(method.getParameterTypes()).map(IClass::fromClass).toArray(IClass[]::new)
        : parameterTypes;
    }

    @Override
    public IClass getReturnType() {
        return returnType == null ? returnType = IClass.fromClass(method.getReturnType()) : returnType;
    }

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }

    @Override
    public Type[] getThrows() {
        return method.getExceptionTypes();
    }
}
