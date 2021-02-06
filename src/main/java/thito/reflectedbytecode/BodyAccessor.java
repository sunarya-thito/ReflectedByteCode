package thito.reflectedbytecode;

import org.objectweb.asm.Opcodes;

public abstract class BodyAccessor implements ArrayReference {

    protected abstract boolean hasInstance();

    @Override
    public void write() {
        if (!hasInstance()) throw new IllegalStateException("static");
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, 0);
    }

    public abstract IClass getSuperClass();
    public abstract LField createVariable();

    public void doneReturn() {
        Code code = Code.getCode();
        code.markReturn();
        code.getCodeVisitor().visitInsn(Opcodes.RETURN);
    }

    public void doneReturn(Object value) {
        Code code = Code.getCode();
        code.markReturn();
        Reference.handleWrite(value);
        code.getCodeVisitor().visitInsn(Opcodes.RETURN);
    }
}
