package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierMethod<T extends GMethod> extends GModifier<T> {
    public GModifierMethod(T member) {
        super(member);
    }
    public GModifierMethod<T> makePublic() {
        return or(Modifier.PUBLIC);
    }
    public GModifierMethod<T> makePrivate() {
        return or(Modifier.PRIVATE);
    }
    public GModifierMethod<T> makeProtected() {
        return or(Modifier.PROTECTED);
    }
    public GModifierMethod<T> makeAbstract() {
        return or(Modifier.ABSTRACT);
    }
    public GModifierMethod<T> makeStrict() {
        return or(Modifier.STRICT);
    }
    public GModifierMethod<T> makeStatic() {
        return or(Modifier.STATIC);
    }
    public GModifierMethod<T> makeFinal() {
        return or(Modifier.FINAL);
    }
    public GModifierMethod<T> makeSynchronized() {
        return or(Modifier.SYNCHRONIZED);
    }
    public GModifierMethod<T> makeNative() {
        return or(Modifier.NATIVE);
    }

    @Override
    GModifierMethod<T> or(int bit) {
        return (GModifierMethod<T>) super.or(bit);
    }
}
