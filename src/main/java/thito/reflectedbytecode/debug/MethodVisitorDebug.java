package thito.reflectedbytecode.debug;

import org.objectweb.asm.*;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.*;
import java.util.*;

public class MethodVisitorDebug extends MethodVisitor {
    static Map<Label, String> labelMapping = new WeakHashMap<>();

    static List<String> op = Arrays.asList("AALOAD", "AASTORE", "ACONST_NULL", "ALOAD", "ALOAD_0", "ALOAD_1", "ALOAD_2", "ALOAD_3", "ANEWARRAY", "ARETURN", "ARRAYLENGTH", "ASTORE", "ASTORE_0", "ASTORE_1", "ASTORE_2", "ASTORE_3", "ATHROW", "BALOAD", "BASTORE", "BIPUSH", "BREAKPOINT", "CALOAD", "CASTORE", "CHECKCAST", "D2F", "D2I", "D2L", "DADD", "DALOAD", "DASTORE", "DCMPG", "DCMPL", "DCONST_0", "DCONST_1", "DDIV", "DLOAD", "DLOAD_0", "DLOAD_1", "DLOAD_2", "DLOAD_3", "DMUL", "DNEG", "DREM", "DRETURN", "DSTORE", "DSTORE_0", "DSTORE_1", "DSTORE_2", "DSTORE_3", "DSUB", "DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1", "DUP2_X2", "F2D", "F2I", "F2L", "FADD", "FALOAD", "FASTORE", "FCMPG", "FCMPL", "FCONST_0", "FCONST_1", "FCONST_2", "FDIV", "FLOAD", "FLOAD_0", "FLOAD_1", "FLOAD_2", "FLOAD_3", "FMUL", "FNEG", "FREM", "FRETURN", "FSTORE", "FSTORE_0", "FSTORE_1", "FSTORE_2", "FSTORE_3", "FSUB", "GETFIELD", "GETSTATIC", "GOTO", "GOTO_W", "I2B", "I2C", "I2D", "I2F", "I2L", "I2S", "IADD", "IALOAD", "IAND", "IASTORE", "ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3", "ICONST_4", "ICONST_5", "IDIV", "IF_ACMPEQ", "IF_ACMPNE", "IF_ICMPEQ", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE", "IF_ICMPLT", "IF_ICMPNE", "IFEQ", "IFGE", "IFGT", "IFLE", "IFLT", "IFNE", "IFNONNULL", "IFNULL", "IINC", "ILOAD", "ILOAD_0", "ILOAD_1", "ILOAD_2", "ILOAD_3", "IMPDEP1", "IMPDEP2", "IMUL", "INEG", "INSTANCEOF", "INVOKEDYNAMIC", "INVOKEINTERFACE", "INVOKESPECIAL", "INVOKESTATIC", "INVOKEVIRTUAL", "IOR", "IREM", "IRETURN", "ISHL", "ISHR", "ISTORE", "ISTORE_0", "ISTORE_1", "ISTORE_2", "ISTORE_3", "ISUB", "IUSHR", "IXOR", "JSR†", "JSR_W†", "L2D", "L2F", "L2I", "LADD", "LALOAD", "LAND", "LASTORE", "LCMP", "LCONST_0", "LCONST_1", "LDC", "LDC_W", "LDC2_W", "LDIV", "LLOAD", "LLOAD_0", "LLOAD_1", "LLOAD_2", "LLOAD_3", "LMUL", "LNEG", "LOOKUPSWITCH", "LOR", "LREM", "LRETURN", "LSHL", "LSHR", "LSTORE", "LSTORE_0", "LSTORE_1", "LSTORE_2", "LSTORE_3", "LSUB", "LUSHR", "LXOR", "MONITORENTER", "MONITOREXIT", "MULTIANEWARRAY", "NEW", "NEWARRAY", "NOP", "POP", "POP2", "PUTFIELD", "PUTSTATIC", "RET†", "RETURN", "SALOAD", "SASTORE", "SIPUSH", "SWAP", "TABLESWITCH", "WIDE");
    static String op(int o) {
        try {
            for (Field field : Opcodes.class.getFields()) {
                if (field.getName().startsWith("V") || field.getName().startsWith("H")  || field.getName().startsWith("F")) continue;
                if (field.getType() == int.class && field.getInt(null) == o && op.contains(field.getName())) {
                    return field.getName();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return String.valueOf(o);
    }
    static String insn(int o) {
        try {
            for (Field field : Opcodes.class.getFields()) {
                if (field.getName().startsWith("V") || field.getName().startsWith("H")  || field.getName().startsWith("F")) continue;
                if (field.getType() == int.class && field.getInt(null) == o && !op.contains(field.getName())) {
                    return field.getName();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return String.valueOf(o);
    }
    static int count = 0;
    static String label(Label label) {
        return labelMapping.computeIfAbsent(label, lbl -> "L"+(count++));
    }
    public MethodVisitorDebug(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    public void visitAttribute(Attribute arg0) {
        System.out.println("visitAttribute("+arg0+")");
        super.visitAttribute(arg0);
    }
    public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
        System.out.println("visitFrame("+op(arg0)+", "+arg1+", "+Arrays.deepToString(arg2)+", "+arg3+", "+Arrays.deepToString(arg4)+")");
        super.visitFrame(arg0, arg1, arg2, arg3, arg4);
    }
    public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
        System.out.println("visitFieldInsn("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        super.visitFieldInsn(arg0, arg1, arg2, arg3);
    }
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        System.out.println("visitAnnotation("+arg0+", "+arg1+")");
        return super.visitAnnotation(arg0, arg1);
    }
    public void visitParameter(String arg0, int arg1) {
        System.out.println("visitParameter("+arg0+", "+arg1+")");
        super.visitParameter(arg0, arg1);
    }
    public void visitCode() {
        System.out.println("visitCode()");
        super.visitCode();
    }
    public void visitIntInsn(int arg0, int arg1) {
        System.out.println("visitIntInsn("+op(arg0)+", "+arg1+")");
        super.visitIntInsn(arg0, arg1);
    }
    public void visitInsn(int arg0) {
        System.out.println("visitInsn("+op(arg0)+")");
        super.visitInsn(arg0);
    }
    public void visitVarInsn(int arg0, int arg1) {
        System.out.println("visitVarInsn("+op(arg0)+", "+arg1+")");
        super.visitVarInsn(arg0, arg1);
    }
    public void visitTypeInsn(int arg0, String arg1) {
        System.out.println("visitTypeInsn("+op(arg0)+", "+arg1+")");
        super.visitTypeInsn(arg0, arg1);
    }
    public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3) {
        System.out.println("visitMethodInsn("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        super.visitMethodInsn(arg0, arg1, arg2, arg3);
    }
    public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3, boolean arg4) {
        System.out.println("visitMethodInsn("+op(arg0)+", "+arg1+", "+arg2+", "+arg3+", "+arg4+")");
        super.visitMethodInsn(arg0, arg1, arg2, arg3, arg4);
    }
    public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
        System.out.println("visitLookupSwitchInsn("+label(arg0)+", "+arg1+", "+arg2+")");
        super.visitLookupSwitchInsn(arg0, arg1, arg2);
    }
    public AnnotationVisitor visitLocalVariableAnnotation(int arg0, TypePath arg1, Label[] arg2, Label[] arg3, int[] arg4, String arg5, boolean arg6) {
        System.out.println("visitLocalVariableAnnotation("+arg0+", "+arg1+", "+arg2+", "+arg3+", "+arg4+", "+arg5+", "+arg6+")");
        return super.visitLocalVariableAnnotation(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
    public AnnotationVisitor visitTypeAnnotation(int arg0, TypePath arg1, String arg2, boolean arg3) {
        System.out.println("visitTypeAnnotation("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        return super.visitTypeAnnotation(arg0, arg1, arg2, arg3);
    }
    public AnnotationVisitor visitInsnAnnotation(int arg0, TypePath arg1, String arg2, boolean arg3) {
        System.out.println("visitInsnAnnotation("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        return super.visitInsnAnnotation(arg0, arg1, arg2, arg3);
    }
    public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label[] arg3) {
        System.out.println("visitTableSwitchInsn("+op(arg0)+", "+arg1+", "+arg2+", "+arg3+")");
        super.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
    }
    public AnnotationVisitor visitAnnotationDefault() {
        System.out.println("visitAnnotationDefault()");
        return super.visitAnnotationDefault();
    }
    public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
        System.out.println("visitLocalVariable("+arg0+", "+arg1+", "+arg2+", "+arg3+", "+arg4+", "+arg5+")");
        super.visitLocalVariable(arg0, arg1, arg2, arg3, arg4, arg5);
    }
    public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
        System.out.println("visitParameterAnnotation("+arg0+", "+arg1+", "+arg2+")");
        return super.visitParameterAnnotation(arg0, arg1, arg2);
    }
    public AnnotationVisitor visitTryCatchAnnotation(int arg0, TypePath arg1, String arg2, boolean arg3) {
        System.out.println("visitTryCatchAnnotation("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        return super.visitTryCatchAnnotation(arg0, arg1, arg2, arg3);
    }
    public void visitAnnotableParameterCount(int arg0, boolean arg1) {
        System.out.println("visitAnnotableParameterCount("+arg0+", "+arg1+")");
        super.visitAnnotableParameterCount(arg0, arg1);
    }
    public void visitInvokeDynamicInsn(String arg0, String arg1, Handle arg2, Object[] arg3) {
        System.out.println("visitInvokeDynamicInsn("+arg0+", "+arg1+", "+arg2+", "+arg3+")");
        super.visitInvokeDynamicInsn(arg0, arg1, arg2, arg3);
    }
    public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
        System.out.println("visitTryCatchBlock("+label(arg0)+", "+label(arg1)+", "+label(arg2)+", "+arg3+")");
        super.visitTryCatchBlock(arg0, arg1, arg2, arg3);
    }
    public void visitMultiANewArrayInsn(String arg0, int arg1) {
        System.out.println("visitMultiANewArrayInsn("+arg0+", "+arg1+")");
        super.visitMultiANewArrayInsn(arg0, arg1);
    }
    public void visitEnd() {
        System.out.println("visitEnd()");
        super.visitEnd();
    }
    public void visitMaxs(int arg0, int arg1) {
        System.out.println("visitMaxs("+arg0+", "+arg1+")");
        super.visitMaxs(arg0, arg1);
    }
    public void visitJumpInsn(int arg0, Label arg1) {
        System.out.println("visitJumpInsn("+op(arg0)+", "+label(arg1)+")");
        super.visitJumpInsn(arg0, arg1);
    }
    public void visitLabel(Label arg0) {
        System.out.println("visitLabel("+label(arg0)+")");
        super.visitLabel(arg0);
    }
    public void visitLdcInsn(Object arg0) {
        System.out.println("visitLdcInsn("+arg0+")");
        super.visitLdcInsn(arg0);
    }
    public void visitLineNumber(int arg0, Label arg1) {
        System.out.println("visitLineNumber("+arg0+", "+label(arg1)+")");
        super.visitLineNumber(arg0, arg1);
    }
    public void visitIincInsn(int arg0, int arg1) {
        System.out.println("visitIincInsn("+op(arg0)+", "+arg1+")");
        super.visitIincInsn(arg0, arg1);
    }
}