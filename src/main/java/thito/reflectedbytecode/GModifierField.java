package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierField<T extends IField & GMember> extends GModifier<T> {
    public GModifierField(T member) {
        super(member);
    }
    public GModifierField<T> makePublic() {
        return or(Modifier.PUBLIC);
    }
    public GModifierField<T> makePrivate() {
        return or(Modifier.PRIVATE);
    }
    public GModifierField<T> makeProtected() {
        return or(Modifier.PROTECTED);
    }
    public GModifierField<T> makeStatic() {
        return or(Modifier.STATIC);
    }
    public GModifierField<T> makeVolatile() {
        return or(Modifier.VOLATILE);
    }
    public GModifierField<T> makeTransient() {
        return or(Modifier.TRANSIENT);
    }
    public GModifierField<T> makeFinal() {
        return or(Modifier.FINAL);
    }

    @Override
    GModifierField<T> or(int bit) {
        return (GModifierField<T>) super.or(bit);
    }
}
