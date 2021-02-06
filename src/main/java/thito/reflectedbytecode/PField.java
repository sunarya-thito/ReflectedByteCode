package thito.reflectedbytecode;

import jdk.internal.org.objectweb.asm.Opcodes;

public class PField implements IParameterField, GMember {

    private int modifiers;
    private IClass type;

    int localIndex;

    public PField(IClass type) {
        this.type = type;
    }

    @Override
    public GModifierLocalField<PField> modifier() {
        return new GModifierLocalField<>(this);
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public void set(Object value) {
        Reference.handleWrite(value);
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ASTORE, localIndex);
    }

    @Override
    public <T extends Reference> T get() {
        return (T) (ArrayReference) () -> {
            Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, localIndex);
        };
    }

    @Override
    public IClass getType() {
        return type;
    }
}
