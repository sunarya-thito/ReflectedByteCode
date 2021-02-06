package thito.reflectedbytecode;

import jdk.internal.org.objectweb.asm.Opcodes;

public class LField implements ILocalField, GMember {

    private int localIndex;
    LField() {
        localIndex = Code.getCode().requestLocalIndex();
    }

    private int modifiers;

    @Override
    public void set(Object value) {
        Reference.handleWrite(value);
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ASTORE, localIndex);
    }

    @Override
    public Reference get() {
        return () -> {
            Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, localIndex);
        };
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public GModifierLocalField<LField> modifier() {
        return new GModifierLocalField<>(this);
    }

    @Override
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}
