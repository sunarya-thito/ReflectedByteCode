package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.Modifier;

public class GModifierClass<T extends GClass> extends GModifierInterface<T> {
    public GModifierClass(T member) {
        super(member);
    }

    public GModifierClass<T> makePublic() {
        return this.or(Opcodes.ACC_PUBLIC);
    }
    public GModifierClass<T> makePrivate() {
        return this.or(Opcodes.ACC_PRIVATE);
    }
    public GModifierClass<T> makeProtected() {
        return this.or(Opcodes.ACC_PROTECTED);
    }
    public GModifierClass<T> makeStatic() {
        return this.or(Opcodes.ACC_STATIC);
    }
    public GModifierClass<T> makeFinal() {
        return this.or(Opcodes.ACC_FINAL);
    }
    public GModifierClass<T> makeStrict() {
        return this.or(Opcodes.ACC_STRICT);
    }
    public GModifierInterface<T> makeAbstract() {
        nAnd(Opcodes.ACC_FINAL);
        return or(Opcodes.ACC_ABSTRACT);
    }
    public GModifierInterface<T> makeInterface() {
        nAnd(Opcodes.ACC_FINAL);
        or(Opcodes.ACC_INTERFACE);
        return or(Opcodes.ACC_ABSTRACT);
    }

    @Override
    GModifierClass<T> or(int bit) {
        return (GModifierClass<T>) super.or(bit);
    }
}
