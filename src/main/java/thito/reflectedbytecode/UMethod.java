package thito.reflectedbytecode;

import java.lang.reflect.*;

public class UMethod extends AbstractMethod {
    private UClass declaring;
    private String name;
    private IClass[] parameterTypes;
    private IClass returnType;
    private int modifier;

    public UMethod(UClass declaring, String name, int modifier, Type... types) {
        this.declaring = declaring;
        this.name = name;
        this.modifier = modifier;
        parameterTypes = new IClass[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] instanceof IClass) {
                parameterTypes[i] = (IClass) types[i];
            } else {
                parameterTypes[i] = new UClass(types[i].getTypeName());
            }
        }
    }

    public UMethod hintReturnType(IClass returnType) {
        this.returnType = returnType;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return modifier;
    }

    @Override
    public IClass getDeclaringClass() {
        return declaring;
    }

    @Override
    public IClass getReturnType() {
        return returnType;
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
