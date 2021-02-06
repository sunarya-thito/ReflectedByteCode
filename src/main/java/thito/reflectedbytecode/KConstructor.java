package thito.reflectedbytecode;

import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor;

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

    @Override
    public void invoke(Object... args) {
        Code code = Code.getCode();
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        for (Object arg : args) {
            Reference.handleWrite(arg);
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
