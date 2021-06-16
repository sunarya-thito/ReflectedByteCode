package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierField<T extends IField & GMember> extends GModifier<T> {
    public GModifierField(T member) {
        super(member);
    }
    public GModifierField<T> makePublic() {
        nAnd(Modifier.PRIVATE);
        nAnd(Modifier.PROTECTED);
        return or(Modifier.PUBLIC);
    }
    public GModifierField<T> makePrivate() {
        nAnd(Modifier.PUBLIC);
        nAnd(Modifier.PROTECTED);
        return or(Modifier.PRIVATE);
    }
    public GModifierField<T> makeProtected() {
        nAnd(Modifier.PUBLIC);
        nAnd(Modifier.PRIVATE);
        return or(Modifier.PROTECTED);
    }
    public GModifierField<T> makeStatic() {
        nAnd(Modifier.ABSTRACT);
        return or(Modifier.STATIC);
    }
    public GModifierField<T> makeVolatile() {
        nAnd(Modifier.FINAL);
        return or(Modifier.VOLATILE);
    }
    public GModifierField<T> makeTransient() {
        return or(Modifier.TRANSIENT);
    }
    public GModifierField<T> makeFinal() {
        nAnd(Modifier.ABSTRACT);
        nAnd(Modifier.VOLATILE);
        return or(Modifier.FINAL);
    }

    @Override
    GModifierField<T> or(int bit) {
        return (GModifierField<T>) super.or(bit);
    }
}
