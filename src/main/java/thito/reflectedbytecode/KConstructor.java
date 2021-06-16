package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Arrays;

public class KConstructor extends AbstractConstructor implements IConstructor {
    private Constructor<?> constructor;
    private IClass[] parameterTypes;
    private IClass declaringClass;

    public KConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public IClass[] getParameterTypes() {
        return parameterTypes == null ? parameterTypes = Arrays.stream(constructor.getParameterTypes()).map(IClass::fromClass).toArray(IClass[]::new) :
                parameterTypes;
    }

    void invoke(Object... args) {
        Code code = Code.getCode();
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        IClass[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Reference.handleWrite(parameterTypes[i], args[i]);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, getDeclaringClass().getRawName(), getName(), getMethodDescriptor(), false);
    }

    @Override
    public Type[] getThrows() {
        return constructor.getExceptionTypes();
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    @Override
    public String getName() {
        return "<init>";
    }

    @Override
    public int getModifiers() {
        return constructor.getModifiers();
    }

    @Override
    public IClass getDeclaringClass() {
        return declaringClass == null ? declaringClass = IClass.fromClass(constructor.getDeclaringClass()) : declaringClass;
    }
}
