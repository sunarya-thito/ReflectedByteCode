package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierConstructor<T extends GConstructor> extends GModifier<T> {
    public GModifierConstructor(T member) {
        super(member);
    }

    public GModifierConstructor<T> makePublic() {
        return or(Modifier.PUBLIC);
    }
    public GModifierConstructor<T> makePrivate() {
        return or(Modifier.PRIVATE);
    }
    public GModifierConstructor<T> makeProtected() {
        return or(Modifier.PROTECTED);
    }

    @Override
    GModifierConstructor<T> or(int bit) {
        return (GModifierConstructor<T>) super.or(bit);
    }
}
