package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;

import java.util.LinkedList;

public class JavaValidatorHelper extends MethodVisitor {

    public static class ValidatorState {
        private LinkedList<ActiveCode> activeCode = new LinkedList<>();
        private boolean freezeChecking;
        public void freeze() {
            freezeChecking = true;
        }
        public void unfreeze() {
            freezeChecking = false;
        }
        public void markActive(ActiveCode activeCode) {
            this.activeCode.addFirst(activeCode);
        }
        public void shutdown() {
            freeze();
            ActiveCode activeCode;
            while ((activeCode = this.activeCode.poll()) != null) {
                activeCode.end();
            }
            this.activeCode.clear();
            unfreeze();
        }
        public void markInactive(ActiveCode activeCode) {
            this.activeCode.remove(activeCode);
        }
    }

    public interface ActiveCode {
        void end();
    }

    static final ThreadLocal<ValidatorState> state = ThreadLocal.withInitial(ValidatorState::new);

    public static ValidatorState getState() {
        return state.get();
    }

    public JavaValidatorHelper(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    public void preCheck() {
        ValidatorState state = getState();
        if (state.freezeChecking) return;
        state.shutdown();
    }

    @Override
    public void visitParameter(String name, int access) {
        preCheck();
        super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        preCheck();
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        preCheck();
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        preCheck();
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        preCheck();
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        preCheck();
        super.visitAttribute(attribute);
    }

    @Override
    public void visitCode() {
        preCheck();
        super.visitCode();
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        preCheck();
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        preCheck();
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        preCheck();
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        preCheck();
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        preCheck();
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        preCheck();
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        preCheck();
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        preCheck();
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        preCheck();
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        preCheck();
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        preCheck();
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        preCheck();
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        preCheck();
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        preCheck();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        preCheck();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        preCheck();
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        preCheck();
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        preCheck();
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        preCheck();
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        preCheck();
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        preCheck();
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        preCheck();
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        preCheck();
        super.visitEnd();
    }
}
