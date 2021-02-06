package thito.reflectedbytecode;

public interface IField extends IMember {
    void set(Object instance, Object value);
    <T extends Reference> T get(Object instance);
    IClass getType();
}
