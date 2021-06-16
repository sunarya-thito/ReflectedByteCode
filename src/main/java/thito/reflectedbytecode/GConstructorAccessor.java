package thito.reflectedbytecode;

import org.objectweb.asm.*;

public class GConstructorAccessor extends MemberBodyAccessor {

    public GConstructorAccessor(GConstructor constructor) {
        super(constructor, constructor.getDeclaringClass());
    }

    @Override
    public GConstructor getMember() {
        return (GConstructor) super.getMember();
    }

    public IClass getSuperClass() {
        return getMember().getDeclaringClass().getSuperClass();
    }

    public void constructDefault() {
        IClass clazz = getSuperClass();
        if (clazz == null) {
            MethodVisitor defaultConstructorVisitor = Code.getCode().getCodeVisitor();
            defaultConstructorVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            defaultConstructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "java/lang/Object", "<init>", "()V", false);
            return;
        }
        clazz.getConstructor().ifPresent(constructor -> {
            ((AbstractConstructor) constructor).invoke();
        });
    }

    public void construct(IConstructor constructor, Object...args) {
        ((AbstractConstructor) constructor).invoke(args);
    }

    @Override
    public void write() {
        Code.getCode().getCodeVisitor().visitVarInsn(Opcodes.ALOAD, 0);
    }
}
