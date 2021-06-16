package thito.reflectedbytecode;

import org.objectweb.asm.*;

import java.lang.reflect.*;

public class GMethodAccessor extends MemberBodyAccessor {
    private GMethod method;

    public GMethodAccessor(GMethod method) {
        super(method, method.getDeclaringClass());
        this.method = method;
    }

    public IClass getSuperClass() {
        return method.getDeclaringClass().getSuperClass();
    }

    @Override
    public void write() {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new UnsupportedOperationException("static");
        }
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, 0);
    }

    @Override
    public void doneReturn() {
        Code code = Code.getCode();
        code.markReturn();
        code.getCodeVisitor().visitInsn(Opcodes.RETURN);
    }

    public void doneReturn(Object value) {
        Code code = Code.getCode();
        code.markReturn();
        Reference.handleWrite(method.getDeclaringClass(), value);
        code.getCodeVisitor().visitInsn(ASMHelper.ToASMType(method.getReturnType()).getOpcode(Opcodes.IRETURN));
    }
}
