package thito.reflectedbytecode;

public interface IField extends IMember {
    void set(Object instance, Object value);
    Reference get(Object instance);
    IClass getType();
}
