package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierInterface<T extends GClass> extends GModifier<T> {
    public GModifierInterface(T member) {
        super(member);
    }
    public GModifierInterface<T> makePublic() {
        return this.or(Modifier.PUBLIC);
    }
    public GModifierInterface<T> makePrivate() {
        return this.or(Modifier.PRIVATE);
    }
    public GModifierInterface<T> makeProtected() {
        return this.or(Modifier.PROTECTED);
    }
    public GModifierInterface<T> makeStatic() {
        return this.or(Modifier.STATIC);
    }
    public GModifierInterface<T> makeStrict() {
        return this.or(Modifier.STRICT);
    }
    @Override
    GModifierInterface<T> or(int bit) {
        return (GModifierInterface<T>) super.or(bit);
    }
}
