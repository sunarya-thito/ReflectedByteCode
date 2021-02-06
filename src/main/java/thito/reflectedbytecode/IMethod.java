package thito.reflectedbytecode;

public interface IMethod extends IParameterizedMember {
    <T extends Reference> T invoke(Object instance, Object...args);
    IClass getReturnType();
}
