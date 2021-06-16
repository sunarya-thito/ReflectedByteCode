package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.*;
import java.lang.reflect.Type;

public abstract class MemberBodyAccessor extends BodyAccessor {

    private final IParameterizedMember member;
    public MemberBodyAccessor(IParameterizedMember member, Type type) {
        super(type);
        this.member = member;
    }

    public IParameterizedMember getMember() {
        return member;
    }

    public abstract IClass getSuperClass();
    public ILocalField createVariable(IClass type) {
        return Code.getCode().getLocalFieldMap().createField(type);
    }
    public ILocalField putVariable(IClass type, Object obj) {
        ILocalField variable = createVariable(type);
        variable.set(obj);
        return variable;
    }

    public void doneReturn() {
        Code code = Code.getCode();
        code.markReturn();
        code.getCodeVisitor().visitInsn(Opcodes.RETURN);
    }

    public Reference getArgument(int index) {
        return Code.getCode().getLocalFieldMap().getField(index).get();
    }
}
