package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierClass<T extends GClass> extends GModifierInterface<T> {
    public GModifierClass(T member) {
        super(member);
    }

    public GModifierClass<T> makePublic() {
        return this.or(Modifier.PUBLIC);
    }
    public GModifierClass<T> makePrivate() {
        return this.or(Modifier.PRIVATE);
    }
    public GModifierClass<T> makeProtected() {
        return this.or(Modifier.PROTECTED);
    }
    public GModifierClass<T> makeStatic() {
        return this.or(Modifier.STATIC);
    }
    public GModifierClass<T> makeFinal() {
        return this.or(Modifier.FINAL);
    }
    public GModifierClass<T> makeStrict() {
        return this.or(Modifier.STRICT);
    }
    public GModifierInterface<T> makeAbstract() {
        nAnd(Modifier.FINAL);
        return or(Modifier.ABSTRACT);
    }
    public GModifierInterface<T> makeInterface() {
        nAnd(Modifier.FINAL);
        or(Modifier.INTERFACE);
        return or(Modifier.ABSTRACT);
    }

    @Override
    GModifierClass<T> or(int bit) {
        return (GModifierClass<T>) super.or(bit);
    }
}
