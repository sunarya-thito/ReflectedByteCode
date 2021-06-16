package thito.reflectedbytecode;

public interface ILocalField {
    void set(Object value);
    Reference get();
    IClass getType();
}
