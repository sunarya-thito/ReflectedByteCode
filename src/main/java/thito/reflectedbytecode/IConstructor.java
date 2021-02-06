package thito.reflectedbytecode;

public interface IConstructor extends IParameterizedMember {
    <T extends Reference> T newInstance(Object...args);
}
