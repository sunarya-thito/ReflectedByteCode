package thito.reflectedbytecode;


import org.objectweb.asm.*;

import java.lang.reflect.*;

public class PField implements IParameterField, GMember {

    private int modifiers;
    private IClass type;

    int localIndex;
    private IParameterizedMember member;

    public PField(IClass type, IParameterizedMember member) {
        this.type = type;
        this.member = member;
    }

    @Override
    public IParameterizedMember getDeclaringMember() {
        return member;
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
        Reference.handleWrite(type, value);
        Code.getCode().getCodeVisitor().visitVarInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.ISTORE), Modifier.isStatic(member.getModifiers()) ?
                localIndex : localIndex + 1);
    }

    @Override
    public Reference get() {
        return new Reference(type) {
            @Override
            public void write() {
                Code.getCode().getCodeVisitor().visitVarInsn(ASMHelper.ToASMType(type).getOpcode(Opcodes.ILOAD), Modifier.isStatic(member.getModifiers()) ?
                        localIndex : localIndex + 1);
            }
        };
    }

    @Override
    public IClass getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PField{" +
                "type=" + type +
                ", localIndex=" + localIndex +
                '}';
    }
}
