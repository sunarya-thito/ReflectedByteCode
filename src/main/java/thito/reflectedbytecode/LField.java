package thito.reflectedbytecode;


import org.objectweb.asm.*;

import java.lang.reflect.Type;

public class LField implements ILocalField, GMember {

    private int localIndex;
    private IClass type;
    public LField(IClass type, int index) {
        this.type = type;
        localIndex = index;
    }

    private int modifiers;

    public int getLocalIndex() {
        return localIndex;
    }

    public void setType(IClass type) {
        this.type = type;
    }

    public void setLocalIndex(int localIndex) {
        this.localIndex = localIndex;
    }

    @Override
    public void set(Object value) {
        Reference.handleWrite(type, value);
        Code.getCode().getCodeVisitor().visitVarInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.ISTORE), localIndex);
    }

    @Override
    public Reference get() {
        return new Reference(type) {
            @Override
            public void write() {
                Code.getCode().getCodeVisitor().visitVarInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.ILOAD), localIndex);
            }
        };
    }

    @Override
    public IClass getType() {
        return type;
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

    @Override
    public String toString() {
        return "LField{" +
                "localIndex=" + localIndex +
                ", type=" + type +
                '}';
    }
}
