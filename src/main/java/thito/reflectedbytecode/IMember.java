package thito.reflectedbytecode;

public interface IMember {
    String getName();
    int getModifiers();
    IClass getDeclaringClass();
}
