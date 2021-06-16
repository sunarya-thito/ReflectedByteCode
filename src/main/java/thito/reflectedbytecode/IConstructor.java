package thito.reflectedbytecode;

public interface IConstructor extends IParameterizedMember {
    void newInstanceVoid(Object...args);
    Reference newInstance(Object...args);
}
