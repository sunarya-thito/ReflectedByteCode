package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.*;
import java.lang.reflect.Type;

public class UConstructor extends AbstractConstructor {

    private IClass[] parameterTypes;
    private UClass declaring;
    public UConstructor(UClass declaring, Type... types) {
        this.declaring = declaring;
        parameterTypes = new IClass[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] instanceof IClass) {
                parameterTypes[i] = (IClass) types[i];
            } else {
                parameterTypes[i] = new UClass(types[i].getTypeName());
            }
        }
    }

    @Override
    void invoke(Object... args) {
        Code code = Code.getCode();
        MethodVisitor visitor = code.getCodeVisitor();
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        for (Object arg : args) {
            Reference.handleWrite(arg);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, getDeclaringClass().getRawName(), "<init>", "()V", false);
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public IClass getDeclaringClass() {
        return declaring;
    }

    @Override
    public int getParameterCount() {
        return parameterTypes.length;
    }

    @Override
    public IClass[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Type[] getThrows() {
        return new Type[0];
    }
}
