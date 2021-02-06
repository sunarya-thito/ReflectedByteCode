package thito.reflectedbytecode;

public interface GMember {
    GModifier<? extends GMember> modifier();
    int getModifiers();
    void setModifiers(int modifiers);
}
