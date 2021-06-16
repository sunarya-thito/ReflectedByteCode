package thito.reflectedbytecode;

public interface IMethod extends IParameterizedMember {
    void invokeVoid(Object instance, Object...args);
    Reference invoke(Object instance, Object... args);
    IClass getReturnType();
}
