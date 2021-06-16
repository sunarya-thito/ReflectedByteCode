package thito.reflectedbytecode.jvm;

import org.objectweb.asm.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.*;
import thito.reflectedbytecode.*;
import thito.reflectedbytecode.debug.*;

import java.lang.reflect.*;
import java.util.*;

public class JavaValidatorHelper extends MethodVisitor {

    private BodyOwner method;
    private MethodVisitor mv;
    private boolean wasThrowing;
    public JavaValidatorHelper(int api, int access, String descriptor, MethodVisitor methodVisitor, BodyOwner method) {
        super(api, new MethodVisitorDebug(api, methodVisitor));
//        super(api, methodVisitor);
        this.method = method;
        this.mv = methodVisitor;
    }

    public void disableCompileDelay() {
        method = null;
    }

    public boolean wasThrowing() {
        return wasThrowing;
    }

    public void preCheck() {
        wasThrowing = false;
//        ValidatorState state = getState();
//        if (state.freezeChecking) return;
//        state.shutdown();
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
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitFrame(type, numLocal, local, numStack, stack);
            });
            return;
        }
        preCheck();
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                if (opcode == Opcodes.ATHROW) {
                    wasThrowing = true;
                }
                super.visitInsn(opcode);
            });
            return;
        }
        preCheck();
        if (opcode == Opcodes.ATHROW) {
            wasThrowing = true;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitIntInsn(opcode, operand);
            });
            return;
        }
        preCheck();
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitVarInsn(opcode, var);
            });
            return;
        }
        preCheck();
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitTypeInsn(opcode, type);
            });
            return;
        }
        preCheck();
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitFieldInsn(opcode, owner, name, descriptor);
            });
            return;
        }
        preCheck();
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitMethodInsn(opcode, owner, name, descriptor);
            });
            return;
        }
        preCheck();
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            });
            return;
        }
        preCheck();
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
            });
            return;
        }
        preCheck();
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitJumpInsn(opcode, label);
            });
            return;
        }
        preCheck();
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitLabel(label);
            });
            return;
        }
        preCheck();
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (!(value instanceof Number || value instanceof Character ||
        value instanceof Boolean || value instanceof Type ||
        value instanceof Handle || value instanceof ConstantDynamic ||
        value instanceof String)) {
            throw new IllegalStateException(""+value);
        }
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitLdcInsn(value);
            });
            return;
        }
        preCheck();
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitIincInsn(var, increment);
            });
            return;
        }
        preCheck();
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitTableSwitchInsn(min, max, dflt, labels);
            });
            return;
        }
        preCheck();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitLookupSwitchInsn(dflt, keys, labels);
            });
            return;
        }
        preCheck();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        if (method != null) {
            method.putPostCompileTask(() -> {
                preCheck();
                super.visitMultiANewArrayInsn(descriptor, numDimensions);
            });
            return;
        }
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
        if (method != null) {
            method.putPreCompileTask(() -> {
                preCheck();
                super.visitTryCatchBlock(start, end, handler, type);
            });
            return;
        }
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
