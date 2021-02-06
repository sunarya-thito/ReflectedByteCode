package thito.reflectedbytecode;

public interface ILocalField {
    void set(Object value);
    <T extends Reference> T get();
}
